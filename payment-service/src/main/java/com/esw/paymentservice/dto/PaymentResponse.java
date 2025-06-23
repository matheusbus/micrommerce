package com.esw.paymentservice.dto;

public record PaymentResponse(
    boolean success,
    String transactionId,
    String message,
    Object extraData
) {
}
