package com.bookbla.americano.domain.payment.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum PaymentExceptionType implements ExceptionType {

    PAYMENT_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "payment-001", "존재하지 않는 결제 타입입니다"),
    NOT_VALID_PAYMENT_ID(HttpStatus.NOT_FOUND, "payment-002", "존재하지 않는 결제 메뉴입니다"),
    RECEIPT_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "payment-003", "해당 결제 정보가 존재하지 않습니다"),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
