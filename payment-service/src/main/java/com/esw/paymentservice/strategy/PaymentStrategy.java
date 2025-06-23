package com.esw.paymentservice.strategy;

import com.esw.paymentservice.dto.PaymentRequest;
import com.esw.paymentservice.dto.PaymentResponse;

public interface PaymentStrategy {
    PaymentResponse process(PaymentRequest request);
}
