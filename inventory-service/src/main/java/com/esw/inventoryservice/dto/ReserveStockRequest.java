package com.esw.inventoryservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record ReserveStockRequest(
    @NotNull(message = "ProductId is required")
    Long productId,

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be a positive integer")
    int quantity,

    UUID orderId
) {}