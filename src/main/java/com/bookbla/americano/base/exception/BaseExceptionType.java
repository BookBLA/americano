package com.bookbla.americano.base.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BaseExceptionType implements ExceptionType {

    TEST_FAIL(HttpStatus.BAD_REQUEST, "-1", "테스트용 예외가 발생했습니다."),
    UNKNOWN_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "5000", "서버가 응답할 수 없습니다."),
    ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST, "4001", "요청 인자가 잘못되었습니다."),
    NOT_VALID_METHODS(HttpStatus.METHOD_NOT_ALLOWED, "4001", "지원하지 않는 메서드입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
