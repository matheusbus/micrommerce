package com.esw.inventoryservice.dto;

public record OrderCreatedEvent(
    Long orderId,
    Long productId,
    int quantity
) {
}
