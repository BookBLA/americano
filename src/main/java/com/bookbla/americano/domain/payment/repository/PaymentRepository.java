package com.bookbla.americano.domain.payment.repository;

import com.bookbla.americano.domain.payment.repository.entity.Payment;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByReceipt(String receipt);

    Optional<Payment> findByOrderId(String orderId);

}
