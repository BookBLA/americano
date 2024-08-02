package com.bookbla.americano.domain.payment.enums;

import java.util.Arrays;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.payment.exception.PaymentExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PaymentTable {

    BOOKMARKS_10("bookmarks_10", 2_500, 10),
    BOOKMARKS_35("bookmarks_35", 7_000, 35),
    BOOKMARKS_80("bookmarks_80", 14_400, 80),
    BOOKMARKS_150("bookmarks_150", 22_500, 150),
    ;

    private final String productId;
    private final int price;
    private final int count;

    public static PaymentTable from(String productId) {
        return Arrays.stream(values())
                .filter(it -> it.getProductId().equalsIgnoreCase(productId))
                .findFirst()
                .orElseThrow(() -> new BaseException(PaymentExceptionType.PAYMENT_TYPE_NOT_FOUND));
    }
}
