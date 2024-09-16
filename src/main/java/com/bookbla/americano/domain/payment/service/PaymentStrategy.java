package com.bookbla.americano.domain.payment.service;

import com.bookbla.americano.domain.payment.enums.PaymentType;
import com.bookbla.americano.domain.payment.repository.Payment;

public interface PaymentStrategy {

    PaymentType getPaymentType();

    Payment getPaymentInformation(String id);

    void getNotificationInformation(String id);

}