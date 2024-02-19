package com.bookbla.americano.domain.memberask.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberAskExceptionType implements ExceptionType {

    INVALID_CONTENT_LENGTH(HttpStatus.BAD_REQUEST, "member-ask_001", "개인 질문은 공백 포함 80자 이하로만 작성가능합니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
