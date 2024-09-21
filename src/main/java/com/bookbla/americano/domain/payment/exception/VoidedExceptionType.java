package com.bookbla.americano.domain.payment.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum VoidedExceptionType  implements ExceptionType {

    VOIDED_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "voided-001", "존재하지 않는 무효 구매 타입입니다"),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
