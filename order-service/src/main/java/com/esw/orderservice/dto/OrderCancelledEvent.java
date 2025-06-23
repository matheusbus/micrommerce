package com.esw.orderservice.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderCancelledEvent(
    UUID orderId,
    Long userId,
    List<OrderItemDTO> items,
    BigDecimal totalAmount
) {
}
