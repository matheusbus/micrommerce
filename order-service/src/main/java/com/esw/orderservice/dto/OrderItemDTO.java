package com.esw.orderservice.dto;

import java.math.BigDecimal;

public record OrderItemDTO (
    Long productId,
    Integer quantity,
    BigDecimal price
) {}
