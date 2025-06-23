package com.esw.orderservice.usecase;

import com.esw.orderservice.dto.OrderCancelledEvent;
import com.esw.orderservice.dto.OrderItemDTO;
import com.esw.orderservice.exception.NotFoundException;
import com.esw.orderservice.exception.OrderCancellationNotAllowedException;
import com.esw.orderservice.kafka.KafkaOrderProducer;
import com.esw.orderservice.model.Order;
import com.esw.orderservice.model.OrderStatus;
import com.esw.orderservice.observer.OrderObserver;
import com.esw.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Service
public class CancelOrderUseCase {

    private final OrderRepository repository;
    private final List<OrderObserver> observers;
    private final KafkaOrderProducer kafkaOrderProducer;

    public CancelOrderUseCase(OrderRepository repository, List<OrderObserver> observers, KafkaOrderProducer kafkaOrderProducer) {
        this.repository = repository;
        this.observers = observers;
        this.kafkaOrderProducer = kafkaOrderProducer;
    }

    @Transactional
    public void handle(String orderId) {
        UUID uuid = UUID.fromString(orderId);

        Order order = repository.findById(uuid)
                .orElseThrow(() -> new NotFoundException(Order.class, "id", uuid.toString()));

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new OrderCancellationNotAllowedException("Order already cancelled.");
        }

        Duration duration = Duration.between(order.getCreatedAt().toInstant(ZoneOffset.of("-03:00")), Instant.now());

        if (duration.toHours() >= 48) {
            throw new OrderCancellationNotAllowedException("Orders older than 48 hours cannot be cancelled..");
        }

        OrderStatus oldStatus = order.getStatus();
        OrderStatus newStatus = OrderStatus.CANCELLED;

        order.setStatus(newStatus);

        repository.save(order);

        kafkaOrderProducer.publishOrderCancelled(mapToEvent(order));

        observers.forEach(observer -> observer.onOrderStatusChanged(order, oldStatus, newStatus));
    }

    private OrderCancelledEvent mapToEvent(Order order) {
        List<OrderItemDTO> items = order.getItems().stream()
                .map(i -> new OrderItemDTO(i.getProductId(),i.getQuantity(), i.getPrice())).toList();

        return new OrderCancelledEvent(order.getId(), order.getUserId(), items, order.getTotalAmount());
    }
}
