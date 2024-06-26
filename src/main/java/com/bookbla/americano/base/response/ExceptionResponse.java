package com.bookbla.americano.base.response;

import com.bookbla.americano.base.exception.ExceptionType;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.Getter;


@Getter
@JsonPropertyOrder({"isSuccess", "code", "message"})
public class ExceptionResponse {

    @Getter(AccessLevel.NONE)
    private final boolean isSuccess;
    private final String code;
    private final String message;

    private ExceptionResponse(String code, String message) {
        this.isSuccess = false;
        this.code = code;
        this.message = message;
    }

    public ExceptionResponse(ExceptionType exceptionType) {
        this(exceptionType.getErrorCode(), exceptionType.getMessage());
    }

    public ExceptionResponse(ExceptionType exceptionType, String message) {
        this(exceptionType.getErrorCode(), message);
    }

    public boolean getIsSuccess() {
        return isSuccess;
    }
}
