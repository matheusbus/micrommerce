package com.esw.inventoryservice.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderCreatedEvent (
        UUID orderId,
        Long userId,
        List<OrderItemDTO> items,
        BigDecimal totalAmount
) {}
