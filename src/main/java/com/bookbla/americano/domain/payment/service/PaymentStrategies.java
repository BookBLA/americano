package com.bookbla.americano.domain.payment.service;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import com.bookbla.americano.domain.payment.enums.PaymentType;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.toMap;


@Component
public class PaymentStrategies {

    private final Map<PaymentType, PaymentStrategy> paymentStrategies;

    public PaymentStrategies(Set<PaymentStrategy> paymentTypes) {
        this.paymentStrategies = paymentTypes.stream()
                .collect(toMap(
                        PaymentStrategy::getPaymentType,
                        Function.identity()
                ));
    }

    public PaymentStrategy get(PaymentType paymentType) {
        return paymentStrategies.get(paymentType);
    }

    public PaymentStrategy get(String value) {
        PaymentType paymentType = PaymentType.from(value);
        return paymentStrategies.get(paymentType);
    }
}
