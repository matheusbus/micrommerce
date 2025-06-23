package com.esw.paymentservice.strategy;

import com.esw.paymentservice.dto.CardDetails;
import com.esw.paymentservice.dto.PaymentRequest;
import com.esw.paymentservice.dto.PaymentResponse;
import com.esw.paymentservice.exception.PaymentException;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;

@Component("DEBIT_CARD")
public class DebitCardPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentResponse process(PaymentRequest request) {
        CardDetails cd = request.cardDetails();
        if (cd == null) {
            throw new PaymentException("Card details not provided.");
        }

        if (cd.cardNumber().length() < 12) {
            throw new PaymentException("Invalid card number.");
        }

        boolean hasBalance = simulateBalanceCheck();
        String transactionId = "DC-" + UUID.randomUUID();

        if (hasBalance) {
            return new PaymentResponse(true, transactionId, "Debit card payment approved.", null);
        } else {
            throw new PaymentException("Debit card payment declined.");
        }
    }

    private boolean simulateBalanceCheck() {
        // Simula processamento com 80% de chance de sucesso
        Random rand = new Random();
        return rand.nextDouble() < 0.8;
    }
}
