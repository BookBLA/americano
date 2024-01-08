package com.bookbla.americano.base.exception;

public interface ExceptionType {

    int getErrorCode();

    String getMessage();

    String getStackTrace();
}
