package com.esw.paymentservice.usecase;

import com.esw.paymentservice.dto.PaymentRequest;
import com.esw.paymentservice.dto.PaymentResponse;
import com.esw.paymentservice.exception.PaymentException;
import com.esw.paymentservice.model.Payment;
import com.esw.paymentservice.model.PaymentMethod;
import com.esw.paymentservice.model.PaymentStatus;
import com.esw.paymentservice.repository.PaymentRepository;
import com.esw.paymentservice.service.PaymentProcessor;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ProcessPaymentUseCase {

    private final PaymentRepository repository;
    private final PaymentProcessor paymentProcessor;

    public ProcessPaymentUseCase(PaymentRepository repository, PaymentProcessor paymentProcessor) {
        this.repository = repository;
        this.paymentProcessor = paymentProcessor;
    }

    @Transactional(dontRollbackOn = PaymentException.class)
    public PaymentResponse handle(PaymentRequest request) {
        Payment payment = new Payment();
        payment.setOrderId(request.orderId());
        payment.setUserId(request.userId());
        payment.setAmount(request.amount());
        payment.setMethod(PaymentMethod.valueOf(request.method().name()));
        payment.setStatus(PaymentStatus.PENDING);
        payment = repository.save(payment);

        try {
            PaymentResponse resp = paymentProcessor.process(request);

            payment.setStatus(PaymentStatus.COMPLETED);
            payment.setTransactionId(resp.transactionId());
            repository.save(payment);

            return resp;

        } catch (PaymentException ex) {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setTransactionId(null);
            repository.save(payment);
            throw ex;
        } catch (Exception ex) {
            payment.setStatus(PaymentStatus.FAILED);
            repository.save(payment);
            throw new PaymentException("Error processing payment: " + ex.getMessage());
        }
    }
}