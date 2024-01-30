package com.bookbla.americano.base.exception;

public class BaseException extends RuntimeException {

    private final ExceptionType exceptionType;

    public BaseException(ExceptionType exceptionType) {
        super(exceptionType.getMessage());
        this.exceptionType = exceptionType;
    }

    public ExceptionType exceptionType() {
        return exceptionType;
    }
}
