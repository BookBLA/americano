package com.bookbla.americano.domain.memberask.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberReplyExceptionType implements ExceptionType {
    IMMUTABLE_POSTCARD(HttpStatus.FORBIDDEN, "member-reply_001", "개인 답변 수정이 불가능한 엽서 입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
