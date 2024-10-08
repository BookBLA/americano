package com.bookbla.americano.domain.payment.service;

import com.bookbla.americano.domain.payment.enums.PaymentType;
import com.bookbla.americano.domain.payment.repository.Payment;
import com.bookbla.americano.domain.payment.repository.PaymentNotification;

public interface PaymentStrategy {

    PaymentType getPaymentType();

    Payment getPaymentInformation(String id);

    PaymentNotification getNotificationInformation(String id);

}