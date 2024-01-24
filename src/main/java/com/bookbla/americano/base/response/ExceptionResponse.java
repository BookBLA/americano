package com.bookbla.americano.base.response;

import com.bookbla.americano.base.exception.ExceptionType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"isSuccess", "code", "message"})
public class ExceptionResponse {

    @JsonProperty("isSuccess")
    private final boolean isSuccess = false;
    private final String errorCode;
    private final String message;

    private ExceptionResponse(String errorCode, String message) {
        this.message = message;
        this.errorCode = errorCode;
    }

    public ExceptionResponse(ExceptionType exceptionType) {
        this(exceptionType.getErrorCode(), exceptionType.getMessage());
    }

    public ExceptionResponse(ExceptionType exceptionType, String message) {
        this(exceptionType.getErrorCode(), message);
    }
}
