package com.bookbla.americano.domain.payment.enums;

import java.util.Arrays;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.payment.excpetion.PaymentExceptionType;
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
                .orElseThrow(() -> new BaseException(PaymentExceptionType.INVALID_PAY_TYPE));
    }
}
