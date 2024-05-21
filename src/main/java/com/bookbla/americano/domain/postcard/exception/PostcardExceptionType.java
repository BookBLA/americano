package com.bookbla.americano.domain.postcard.exception;


import com.bookbla.americano.base.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PostcardExceptionType implements ExceptionType {

    POSTCARD_TYPE_NOT_VALID(HttpStatus.NOT_FOUND, "postcard_001", "유효하지 않은 엽서 타입입니다."),
    INVALID_PAY_TYPE(HttpStatus.BAD_REQUEST, "postcard_002", "유효하지 않은 엽서 가격 유형입니다."),

    PENDING_POSTCARD_EXISTS(HttpStatus.BAD_REQUEST, "postcard-003", "대기중인 엽서가 존재합니다."),
    ACCEPTED_POSTCARD_EXISTS(HttpStatus.BAD_REQUEST, "postcard-004", "매칭된 엽서가 존재합니다."),
    ALL_WRONG_POSTCARD_EXISTS(HttpStatus.BAD_REQUEST, "postcard-005", "독서 퀴즈를 모두 틀린 엽서가 존재합니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
