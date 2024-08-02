package com.bookbla.americano.domain.payment.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum PaymentExceptionType implements ExceptionType {

    PAYMENT_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "payment-001", "존재하지 않는 결제 타입입니다"),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
