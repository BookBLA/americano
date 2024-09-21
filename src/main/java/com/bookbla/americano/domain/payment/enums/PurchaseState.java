package com.bookbla.americano.domain.payment.enums;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.payment.exception.PurchaseStateExceptionType;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PurchaseState {
    PURCHASED(0),
    CANCELED(1),
    PENDING(2);

    private final int value;

    public static PurchaseState from(int value) {
        return Arrays.stream(values())
            .filter(it -> it.getValue() == value)
            .findFirst()
            .orElseThrow(() -> new BaseException(PurchaseStateExceptionType.PURCHASE_STATE_NOT_FOUND));
    }
}
