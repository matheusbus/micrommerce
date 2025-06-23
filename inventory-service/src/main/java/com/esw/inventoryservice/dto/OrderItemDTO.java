package com.esw.inventoryservice.dto;

import java.math.BigDecimal;

public record OrderItemDTO (
        Long productId,
        Integer quantity,
        BigDecimal price
) {}
