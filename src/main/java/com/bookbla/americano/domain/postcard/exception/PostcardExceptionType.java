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

    READING_POSTCARD_EXISTS(HttpStatus.BAD_REQUEST, "postcard-002", "상대방이 읽은 엽서가 존재합니다."),
    PENDING_POSTCARD_EXISTS(HttpStatus.BAD_REQUEST, "postcard-003", "대기중인 엽서가 존재합니다."),
    ACCEPTED_POSTCARD_EXISTS(HttpStatus.BAD_REQUEST, "postcard-004", "매칭된 엽서가 존재합니다."),
    ALL_WRONG_POSTCARD_EXISTS(HttpStatus.BAD_REQUEST, "postcard-005", "독서 퀴즈를 모두 틀린 엽서가 존재합니다."),

    INVALID_POSTCARD_STATUS(HttpStatus.BAD_REQUEST, "postcard-006", "유효하지 않은 엽서 상태입니다."),
    INVALID_POSTCARD(HttpStatus.BAD_REQUEST, "postcard-007", "유효하지 않은 엽서 아이디입니다."),
    ACCESS_DENIED_TO_POSTCARD(HttpStatus.FORBIDDEN, "postcard-008", "해당 엽서에 대한 접근 권한이 없습니다."),

    READ_POSTCARD_ALREADY(HttpStatus.BAD_REQUEST, "postcard-009", "이미 읽은 엽서입니다."),
    ALL_WRONG_POSTCARD(HttpStatus.BAD_REQUEST, "postcard-010", "독서 퀴즈를 모두 틀린 엽서입니다."),

    POSTCARD_NOT_ACCEPTED(HttpStatus.UNPROCESSABLE_ENTITY, "postcard-011", "수락되지 않은 엽서입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
