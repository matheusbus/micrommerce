package com.esw.paymentservice.controller;

import com.esw.paymentservice.dto.PaymentRequest;
import com.esw.paymentservice.dto.PaymentResponse;
import com.esw.paymentservice.usecase.ProcessPaymentUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final ProcessPaymentUseCase useCase;

    public PaymentController(ProcessPaymentUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> pay(@RequestBody @Valid PaymentRequest request) {
        PaymentResponse resp = useCase.handle(request);
        return ResponseEntity.ok(resp);
    }
}
