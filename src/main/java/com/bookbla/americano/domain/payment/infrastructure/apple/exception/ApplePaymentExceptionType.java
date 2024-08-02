package com.bookbla.americano.domain.payment.infrastructure.apple.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ApplePaymentExceptionType implements ExceptionType {

    INVALID_APPLE_KEY(HttpStatus.BAD_REQUEST, "apple-payment-001", "루트 인증서가 애플의 인증서와 일치하지 않습니다."),
    CERTIFICATION_CHAIN_INVALID(HttpStatus.BAD_REQUEST, "apple-payment-002", "인증서 체인이 서로 암호화되지 않았습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
