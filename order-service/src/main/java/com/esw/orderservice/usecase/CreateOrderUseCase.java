package com.esw.orderservice.usecase;

import com.esw.orderservice.dto.CreateOrderRequest;
import com.esw.orderservice.dto.OrderCreatedEvent;
import com.esw.orderservice.dto.OrderItemDTO;
import com.esw.orderservice.kafka.KafkaOrderProducer;
import com.esw.orderservice.model.Order;
import com.esw.orderservice.model.OrderItem;
import com.esw.orderservice.model.OrderStatus;
import com.esw.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CreateOrderUseCase {

    private final OrderRepository repository;
    private final KafkaOrderProducer kafkaOrderProducer;

    public CreateOrderUseCase(OrderRepository repository, KafkaOrderProducer kafkaOrderProducer) {
        this.repository = repository;
        this.kafkaOrderProducer = kafkaOrderProducer;
    }

    public Order handle(CreateOrderRequest request) {
        Order order = new Order();
        order.setUserId(request.userId());
        order.setStatus(OrderStatus.CREATED);

        List<OrderItem> items = request.items().stream().map(itemReq -> {
            OrderItem item = new OrderItem();
            item.setProductId(itemReq.productId());
            item.setQuantity(itemReq.quantity());
            item.setPrice(itemReq.price());
            item.setOrder(order);
            return item;
        }).toList();

        order.setItems(items);

        BigDecimal total = items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(total);

        Order saved = repository.save(order);

        kafkaOrderProducer.publishOrderCreated(mapToEvent(order));

        return saved;
    }

    private OrderCreatedEvent mapToEvent(Order order) {
        List<OrderItemDTO> items = order.getItems().stream()
                .map(i -> new OrderItemDTO(i.getProductId(),i.getQuantity(), i.getPrice())).toList();

        return new OrderCreatedEvent(order.getId(), order.getUserId(), items, order.getTotalAmount());
    }
}
