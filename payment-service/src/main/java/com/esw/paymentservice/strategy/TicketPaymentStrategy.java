package com.esw.paymentservice.strategy;

import com.esw.paymentservice.dto.PaymentRequest;
import com.esw.paymentservice.dto.PaymentResponse;
import com.esw.paymentservice.exception.PaymentException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Component("TICKET")
public class TicketPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentResponse process(PaymentRequest request) {

        BigDecimal amount = request.amount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new PaymentException("Ticket invalid value.");
        }

        String transactionId = "TICKET-" + UUID.randomUUID();
        LocalDate dueDate = LocalDate.now().plusDays(3);
        String ticketUrl = "https://bill.example.com/pay/" + transactionId;

        var extra = new TicketData(ticketUrl, dueDate.toString(), "34191.79001 01043.510047 91020.150008 8 87670000010000");

        return new PaymentResponse(true, transactionId, "Bill generated successfully. Due date: " + dueDate, extra);
    }

    private record TicketData(
        String ticketUrl,
        String dueDate,
        String writableLine
    ) {
    }
}
