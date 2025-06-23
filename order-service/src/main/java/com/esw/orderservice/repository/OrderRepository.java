package com.esw.orderservice.repository;

import com.esw.orderservice.model.Order;
import com.esw.orderservice.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByUserIdAndStatus(Long userId, OrderStatus orderStatus);
}
