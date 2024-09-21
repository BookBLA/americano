package com.bookbla.americano.domain.payment.repository;

import com.bookbla.americano.domain.payment.repository.entity.PaymentVoidPurchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentVoidPurchaseRepository extends JpaRepository<PaymentVoidPurchase, Long> {

}
