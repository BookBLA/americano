package com.bookbla.americano.domain.postcard.enums;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.postcard.exception.PostcardExceptionType;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PostcardStatus {

    PENDING("PENDING"),
    READ("READ"),
    ACCEPT("ACCEPT"),
    REFUSED("REFUSED"),
    ALL_WRONG("ALL_WRONG");

    private final String status;

    public boolean isRefused() {
        return this == REFUSED;
    }

    public static PostcardStatus from(String status) {
        return Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(status))
                .findAny()
                .orElseThrow(
                        () -> new BaseException(PostcardExceptionType.INVALID_POSTCARD_STATUS));
    }

    @JsonValue
    public String getStatus() {
        return status;
    }
}
