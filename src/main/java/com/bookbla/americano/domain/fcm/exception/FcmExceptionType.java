package com.bookbla.americano.domain.fcm.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FcmExceptionType implements ExceptionType {

    ACCESS_TOKEN_FAIL(HttpStatus.FORBIDDEN, "fcm_001", "토큰을 가져오는데 실패"),
    ;
    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
