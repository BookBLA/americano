package com.bookbla.americano.domain.payment.service;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.bookbla.americano.domain.payment.enums.PaymentType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PaymentStrategies {

    private Map<PaymentType, PaymentStrategy> paymentStrategies = new EnumMap<>(PaymentType.class);

    private PaymentStrategies(Set<PaymentStrategy> paymentStrategies) {
        this.paymentStrategies = paymentStrategies.stream()
                .collect(Collectors.toMap(
                        PaymentStrategy::getPaymentType,
                        Function.identity()));
    }

    public PaymentStrategy get(PaymentType paymentType) {
        return paymentStrategies.get(paymentType);
    }

}
