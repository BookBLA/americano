package com.bookbla.americano.base.exception;

import org.springframework.http.HttpStatus;

public interface ExceptionType {

    HttpStatus getHttpStatus();

    String getErrorCode();

    String getMessage();

}
