package com.esw.orderservice.usecase;

import com.esw.orderservice.dto.CreateOrderRequest;
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

    public CreateOrderUseCase(OrderRepository repository) {
        this.repository = repository;
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

        /*
        kafkaTemplate.send("order.created", new OrderCreatedEvent(
                saved.getId(),
                saved.getUserId(),
                total,
                saved.getItems().stream().map(i ->
                        new OrderCreatedEvent.OrderItemDTO(i.getProductId(), i.getQuantity())
                ).toList()
        ));
        */

        return saved;
    }
}
