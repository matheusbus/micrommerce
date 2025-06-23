package com.esw.paymentservice.strategy;

import com.esw.paymentservice.dto.PaymentRequest;
import com.esw.paymentservice.dto.PaymentResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("PIX")
public class PixPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentResponse process(PaymentRequest request) {
        // Aqui simulamos sucesso imediato e geramos um ID de transação
        String transactionId = "PIX-" + UUID.randomUUID();

        return new PaymentResponse(true, transactionId, "Payment via Pix made successfully.", null);
    }
}
