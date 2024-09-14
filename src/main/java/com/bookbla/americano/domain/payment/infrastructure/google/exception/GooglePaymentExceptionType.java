package com.bookbla.americano.domain.payment.infrastructure.google.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum GooglePaymentExceptionType implements ExceptionType {

    JSON_IO_ERROR(HttpStatus.BAD_REQUEST, "google-payment-001", "Google API 통신 중 IO 오류 발생했습니다."),
    SECURITY_ERROR(HttpStatus.BAD_REQUEST, "google-payment-002", "Google API 인증 또는 보안 설정 오류가 발생했습니다."),
    NOT_PURCHASE(HttpStatus.BAD_REQUEST, "google-payment-003", "Google API 인증 또는 보안 설정 오류가 발생했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
