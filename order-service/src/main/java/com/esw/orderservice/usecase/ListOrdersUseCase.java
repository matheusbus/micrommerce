package com.esw.orderservice.usecase;

import com.esw.orderservice.dto.OrderItemResponse;
import com.esw.orderservice.dto.OrderResponse;
import com.esw.orderservice.model.Order;
import com.esw.orderservice.model.OrderStatus;
import com.esw.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListOrdersUseCase {

    private final OrderRepository repository;

    public ListOrdersUseCase(OrderRepository repository) {
        this.repository = repository;
    }

    public List<OrderResponse> handle(Long userId, String status) {
        OrderStatus orderStatus = OrderStatus.valueOf(status);

        List<Order> orders = repository.findByUserIdAndStatus(userId, orderStatus);
        return orders.stream().map(this::toResponse).toList();
    }

    private OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getUserId(),
                order.getItems().stream().map(item ->
                        new OrderItemResponse(item.getProductId(), item.getQuantity(), item.getPrice())
                ).toList(),
                order.getStatus(),
                order.getTotalAmount()
        );
    }
}
