package com.esw.paymentservice.dto;

import com.esw.paymentservice.model.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PaymentRequest (
    @NotNull(message = "OrderId is required")
    String orderId,

    @NotNull(message = "UserId is required")
    Long userId,

    @NotNull(message = "PaymentMethod is required")
    PaymentMethod method,

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be a positive bigDecimal")
    BigDecimal amount,

    CardDetails cardDetails
) {}
