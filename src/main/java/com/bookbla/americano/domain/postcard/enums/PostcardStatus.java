package com.bookbla.americano.domain.postcard.enums;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.postcard.exception.PostcardExceptionType;

import java.util.Arrays;

public enum PostcardStatus {

    PENDING, READ, ACCEPT, REFUSED, ALL_WRONG;

    public boolean isRefused() {
        return this == REFUSED;
    }

    public static PostcardStatus from(String status) {
        return Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(status))
                .findAny()
                .orElseThrow(() -> new BaseException(PostcardExceptionType.INVALID_POSTCARD_STATUS));
    }
}
