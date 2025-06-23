package com.esw.inventoryservice.dto;

import jakarta.validation.constraints.NotNull;

public record ResetStockRequest(
    @NotNull(message = "ProductId is required")
    Long productId
) {}
