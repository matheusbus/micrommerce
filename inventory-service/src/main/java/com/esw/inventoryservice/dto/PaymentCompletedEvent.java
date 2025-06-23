package com.esw.inventoryservice.dto;

public record PaymentCompletedEvent (
    Long orderId,
    Long productId,
    int quantity
) {}
