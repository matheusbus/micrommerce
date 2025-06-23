package com.esw.inventoryservice.dto;

public record StockReservedEvent (
    Long orderId,
    boolean success
) {}