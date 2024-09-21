package com.bookbla.americano.domain.payment.enums;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.payment.exception.VoidedExceptionType;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum VoidedSource {
    USER(0),
    DEVELOPER(1),
    GOOGLE(2);

    private final int value;

    public static VoidedSource from(int value) {
        return Arrays.stream(values())
            .filter(it -> it.getValue() == value)
            .findFirst()
            .orElseThrow(() -> new BaseException(VoidedExceptionType.VOIDED_TYPE_NOT_FOUND));
    }
}
