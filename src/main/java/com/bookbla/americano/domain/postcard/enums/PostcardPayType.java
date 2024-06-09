package com.bookbla.americano.domain.postcard.enums;

import java.util.Arrays;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.postcard.exception.PostcardExceptionType;

public enum PostcardPayType {
    FREE, PAY;

    public static PostcardPayType from(String type) {
        return Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(type))
                .findAny()
                .orElseThrow(() -> new BaseException(PostcardExceptionType.INVALID_PAY_TYPE));
    }
}
