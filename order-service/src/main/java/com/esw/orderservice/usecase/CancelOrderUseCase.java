package com.esw.orderservice.usecase;

import com.esw.orderservice.exception.NotFoundException;
import com.esw.orderservice.exception.OrderCancellationNotAllowedException;
import com.esw.orderservice.model.Order;
import com.esw.orderservice.model.OrderStatus;
import com.esw.orderservice.observer.OrderObserver;
import com.esw.orderservice.repository.OrderRepository;
import com.esw.orderservice.service.OrderStatusManager;
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
    private final OrderStatusManager orderStatusManager;
    private final List<OrderObserver> observers;

    public CancelOrderUseCase(OrderRepository repository, OrderStatusManager orderStatusManager, List<OrderObserver> observers) {
        this.repository = repository;
        this.orderStatusManager = orderStatusManager;
        this.observers = observers;
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

        observers.forEach(observer -> observer.onOrderStatusChanged(order, oldStatus, newStatus));
    }
}
