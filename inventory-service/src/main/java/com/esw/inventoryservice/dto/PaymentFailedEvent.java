package com.esw.inventoryservice.dto;

public record PaymentFailedEvent (
    Long orderId,
    Long productId,
    int quantity
) {}
