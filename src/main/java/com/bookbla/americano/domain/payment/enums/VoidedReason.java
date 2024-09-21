package com.bookbla.americano.domain.payment.enums;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.payment.exception.VoidedExceptionType;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum VoidedReason {
    OTHER(0),
    REMORSE(1),
    NOT_RECEIVED(2),
    DEFECTIVE(3),
    ACCIDENTAL_PURCHASE(4),
    FRAUD(5),
    FRIENDLY_FRAUD(6),
    CHARGEBACK(7),
    UNACKNOWLEDGED_PURCHASE(8);

    private final int value;

    public static VoidedReason from(int value) {
        return Arrays.stream(values())
            .filter(it -> it.getValue() == value)
            .findFirst()
            .orElseThrow(() -> new BaseException(VoidedExceptionType.VOIDED_TYPE_NOT_FOUND));
    }
}
