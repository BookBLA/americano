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

    READING_POSTCARD_EXISTS(HttpStatus.BAD_REQUEST, "postcard-003", "상대방이 읽은 엽서가 존재합니다."),
    PENDING_POSTCARD_EXISTS(HttpStatus.BAD_REQUEST, "postcard-004", "대기중인 엽서가 존재합니다."),
    ACCEPTED_POSTCARD_EXISTS(HttpStatus.BAD_REQUEST, "postcard-005", "매칭된 엽서가 존재합니다."),
    ALL_WRONG_POSTCARD_EXISTS(HttpStatus.BAD_REQUEST, "postcard-006", "독서 퀴즈를 모두 틀린 엽서가 존재합니다."),
    BLOCKED(HttpStatus.BAD_REQUEST, "postcard-007", "상대방이 차단되어 엽서를 보낼 수 없습니다."),

    INVALID_POSTCARD_STATUS(HttpStatus.BAD_REQUEST, "postcard-008", "유효하지 않은 엽서 상태입니다."),
    INVALID_POSTCARD(HttpStatus.BAD_REQUEST, "postcard-009", "유효하지 않은 엽서 아이디입니다."),
    ACCESS_DENIED_TO_POSTCARD(HttpStatus.FORBIDDEN, "postcard-010", "해당 엽서에 대한 접근 권한이 없습니다."),

    READ_POSTCARD_ALREADY(HttpStatus.BAD_REQUEST, "postcard-011", "이미 읽은 엽서입니다."),
    ALL_WRONG_POSTCARD(HttpStatus.BAD_REQUEST, "postcard-012", "독서 퀴즈를 모두 틀린 엽서입니다."),

    POSTCARD_NOT_ACCEPTED(HttpStatus.UNPROCESSABLE_ENTITY, "postcard-013", "수락되지 않은 엽서입니다."),
    POSTCARD_RECEIVE_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "postcard-014", "엽서를 받은 회원의 id가 존재하지 않습니다."),
    POSTCARD_STATUS_REFUSED_AT_NOT_SAVED(HttpStatus.BAD_REQUEST, "postcard-015", "엽서 거절 시간이 누락되었습니다."),
    POSTCARD_STATUS_REFUSED_AT_PERIOD_EXCEEDED(HttpStatus.BAD_REQUEST, "postcard-016", "엽서 거절 후 14일이 지났습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
