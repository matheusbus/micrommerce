package com.esw.orderservice.model;

public enum OrderStatus {
    CREATED,
    INVENTORY_RESERVED,
    PAYMENT_COMPLETED,
    PAYMENT_FAILED,
    CANCELLED
}
