package com.esw.inventoryservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ReserveStockRequest(
    @NotNull(message = "ProductId is required")
    Long productId,

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be a positive integer")
    int quantity,

    Long orderId
) {}