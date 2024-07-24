package com.bookbla.americano.domain.payment.repository;

import com.bookbla.americano.domain.payment.repository.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
