package com.bookbla.americano.base.exception;

public class BaseException extends RuntimeException {

    private final ExceptionType exceptionType;

    public BaseException(ExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    public ExceptionType exceptionType() {
        return exceptionType;
    }
}
