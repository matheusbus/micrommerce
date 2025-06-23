package com.esw.orderservice.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderRequest(
    @NotNull(message = "UserId is required")
    Long userId,

    List<CreateOrderItemRequest> items
) {
}
