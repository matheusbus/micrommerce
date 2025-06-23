package com.esw.orderservice.usecase;

import com.esw.orderservice.dto.OrderItemResponse;
import com.esw.orderservice.dto.OrderResponse;
import com.esw.orderservice.exception.NotFoundException;
import com.esw.orderservice.model.Order;
import com.esw.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetOrderByIdUseCase {
    private final OrderRepository repository;

    public GetOrderByIdUseCase(OrderRepository repository) {
        this.repository = repository;
    }

    public OrderResponse handle(String id) {
        UUID uuid = UUID.fromString(id);

        return repository.findById(uuid)
                .map(order -> new OrderResponse(
                        order.getId(),
                        order.getUserId(),
                        order.getItems().stream().map(item ->
                                new OrderItemResponse(item.getProductId(), item.getQuantity(), item.getPrice())
                        ).toList(),
                        order.getStatus(),
                        order.getTotalAmount()
                ))
                .orElseThrow(() -> new NotFoundException(Order.class, "id", id));
    }
}
