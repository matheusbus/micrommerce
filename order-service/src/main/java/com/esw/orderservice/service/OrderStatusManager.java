package com.esw.orderservice.service;

import com.esw.orderservice.exception.NotFoundException;
import com.esw.orderservice.model.Order;
import com.esw.orderservice.model.OrderStatus;
import com.esw.orderservice.observer.OrderObserver;
import com.esw.orderservice.repository.OrderRepository;
import com.esw.orderservice.usecase.GetOrderByIdUseCase;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderStatusManager {

    private final OrderRepository repository;
    private final List<OrderObserver> observers;

    public OrderStatusManager(OrderRepository repository, List<OrderObserver> observers, GetOrderByIdUseCase getOrderByIdUseCase) {
        this.repository = repository;
        this.observers = observers;
    }

    public void updateStatus(UUID orderId, OrderStatus newStatus) {
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(Order.class, "id", orderId.toString()));

        OrderStatus oldStatus = order.getStatus();
        order.setStatus(newStatus);
        repository.save(order);

        observers.forEach(observer -> observer.onOrderStatusChanged(order, oldStatus, newStatus));
    }
}
