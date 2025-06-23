package com.esw.paymentservice.repository;

import com.esw.paymentservice.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    List<Payment> findByOrderId(String orderId);
    List<Payment> findByUserId(Long userId);
}
