package com.bookbla.americano.domain.payment.repository;

import com.bookbla.americano.domain.payment.repository.entity.PaymentNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentNotificationRepository extends JpaRepository<PaymentNotification, Long> {

}
