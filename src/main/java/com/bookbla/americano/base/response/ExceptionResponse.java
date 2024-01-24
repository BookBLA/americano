package com.bookbla.americano.base.response;

import com.bookbla.americano.base.exception.ExceptionType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"isSuccess", "code", "message"})
public class ExceptionResponse {

    @JsonProperty("isSuccess")
    private final boolean isSuccess;
    private final String errorCode;
    private final String message;

    public ExceptionResponse(ExceptionType exceptionType) {
        this.isSuccess = false;
        this.errorCode = exceptionType.getErrorCode();
        this.message = exceptionType.getMessage();
    }
}
