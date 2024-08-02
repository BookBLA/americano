package com.bookbla.americano.domain.payment.service;

import com.bookbla.americano.domain.payment.enums.PaymentType;

public interface PaymentStrategy {

    PaymentType getPaymentType();

}