package com.bookbla.americano.domain.payment.enums;

import java.util.Arrays;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.payment.exception.PaymentExceptionType;
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
                .orElseThrow(() -> new BaseException(PaymentExceptionType.PAYMENT_TYPE_NOT_FOUND));
    }
}
