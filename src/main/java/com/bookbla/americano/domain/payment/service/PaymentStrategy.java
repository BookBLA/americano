package com.bookbla.americano.domain.payment.service;

import com.bookbla.americano.domain.payment.repository.entity.PaymentType;
import com.bookbla.americano.domain.payment.repository.entity.PaymentHistory;

public interface PaymentStrategy {

    PaymentType getPaymentType();

    PaymentHistory getPaymentInformation(String id);

}
