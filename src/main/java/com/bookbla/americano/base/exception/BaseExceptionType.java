package com.bookbla.americano.base.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BaseExceptionType implements ExceptionType {

    TEST_FAIL("-1", "테스트 생성 중 예외가 발생했습니다", "asd".repeat(3)),
    INTERNAL_SERVER_ERROR("5000", "글로벌 서버 에러", "에러는 다음과 같아용"),
    ARGUMENT_NOT_VALID("4001", "안맞아용", "MethodArgumentNotValidException 어캐할까..."),
    ;

    private final String errorCode;
    private final String message;
    private final String stackTrace;

    BaseExceptionType(int errorCode, String message, String stackTrace) {
        this(String.valueOf(errorCode), message, stackTrace);
    }

}
