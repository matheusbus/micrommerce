package com.esw.paymentservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CardDetails(
    @NotNull(message = "CardNumber is required")
    String cardNumber,

    @NotNull(message = "CardHolderName is required")
    String cardHolderName,

    @NotNull(message = "Expiry is required")
    @Pattern( regexp="\\d{2}/\\d{2}", message="Format: MM/YY")
    String expiry,

    @NotNull(message = "CVV is required")
    @Pattern(regexp="\\d{3,4}", message="Invalid CVV for Credit/Debit Card")
    String cvv
) {
}
