package com.esw.inventoryservice.dto;

import java.util.UUID;

public record StockReservedEvent (
    UUID orderId,
    boolean success
) {}