package com.esw.paymentservice.service;

import com.esw.paymentservice.dto.PaymentRequest;
import com.esw.paymentservice.dto.PaymentResponse;
import com.esw.paymentservice.exception.PaymentException;
import com.esw.paymentservice.strategy.PaymentStrategy;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PaymentProcessor {

    private final Map<String, PaymentStrategy> strategies;

    public PaymentProcessor(Map<String, PaymentStrategy> strategies) {
        this.strategies = strategies;
    }

    public PaymentResponse process(PaymentRequest request) {
        String key = request.method().name();
        PaymentStrategy strategy = strategies.get(key);

        if (strategy == null) {
            throw new PaymentException("Payment method not supported: " + key);
        }

        return strategy.process(request);
    }
}