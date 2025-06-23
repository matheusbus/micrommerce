package com.esw.paymentservice.strategy;

import com.esw.paymentservice.dto.CardDetails;
import com.esw.paymentservice.dto.PaymentRequest;
import com.esw.paymentservice.dto.PaymentResponse;
import com.esw.paymentservice.exception.PaymentException;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.util.Random;
import java.util.UUID;

@Component("CREDIT_CARD")
public class CreditCardPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentResponse process(PaymentRequest request) {
        CardDetails cd = request.cardDetails();
        if (cd == null) {
            throw new PaymentException("Card details not provided.");
        }

        if (!isValidCardNumber(cd.cardNumber())) {
            throw new PaymentException("Invalid card number.");
        }
        if (isExpired(cd.expiry())) {
            throw new PaymentException("Expired card.");
        }
        if (!isValidCvv(cd.cvv())) {
            throw new PaymentException("Invalid CVV.");
        }

        boolean approved = simulateCreditAuthorization();
        String transactionId = "CC-" + UUID.randomUUID();

        if (approved) {
            return new PaymentResponse(true, transactionId, "Credit card payment approved.", null);
        } else {
            throw new PaymentException("Credit card payment declined.");
        }
    }

    private boolean isValidCardNumber(String number) {
        return number.length() >= 12 && number.length() <= 19;
    }
    private boolean isExpired(String expiry) {
        try {
            YearMonth ym = YearMonth.parse(expiry, java.time.format.DateTimeFormatter.ofPattern("MM/yy"));
            return YearMonth.now().isAfter(ym);
        } catch (Exception e) {
            return true;
        }
    }
    private boolean isValidCvv(String cvv) {
        return cvv.matches("\\d{3,4}");
    }

    private boolean simulateCreditAuthorization() {
        // Simula processamento com 80% de chance de sucesso
        Random rand = new Random();
        return rand.nextDouble() < 0.8;
    }
}
