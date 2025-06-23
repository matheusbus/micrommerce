package com.esw.orderservice.dto;

import com.esw.orderservice.model.OrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
    UUID id,
    Long userId,
    List<OrderItemResponse> items,
    OrderStatus status,
    BigDecimal totalAmount
) {
}
