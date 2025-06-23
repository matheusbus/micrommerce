package com.esw.orderservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateOrderItemRequest(
    @NotNull(message = "ProductId is required")
    Long productId,

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be a positive integer")
    Integer quantity,

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be a positive integer")
    BigDecimal price
) {
}
