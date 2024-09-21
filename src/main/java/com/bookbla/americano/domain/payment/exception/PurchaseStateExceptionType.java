package com.bookbla.americano.domain.payment.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum PurchaseStateExceptionType implements ExceptionType {

    PURCHASE_STATE_NOT_FOUND(HttpStatus.NOT_FOUND, "purchase-state-001", "존재하지 않는 결제 상태 타입입니다"),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
