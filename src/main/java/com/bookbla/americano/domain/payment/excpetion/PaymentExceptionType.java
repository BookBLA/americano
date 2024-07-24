package com.bookbla.americano.domain.payment.excpetion;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum PaymentExceptionType implements ExceptionType {

    INVALID_PAY_TYPE(HttpStatus.NOT_FOUND, "payment-001", "존재하지 않는 결제 수단입니다"),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
