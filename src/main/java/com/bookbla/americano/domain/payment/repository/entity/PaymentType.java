package com.bookbla.americano.domain.payment.repository.entity;

import java.util.Arrays;

import lombok.Getter;

@Getter
public enum PaymentType {

    APPLE,
    GOOGLE,
    ;

    public static PaymentType from(String name) {
        return Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid payment type: " + name));
    }
}
