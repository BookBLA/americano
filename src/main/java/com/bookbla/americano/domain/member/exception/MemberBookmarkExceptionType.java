package com.bookbla.americano.domain.member.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberBookmarkExceptionType implements ExceptionType {

    INVALID_BOOKMARK_COUNTS(HttpStatus.BAD_REQUEST, "member-bookmark_001", "책갈피 개수가 부족합니다."),
    MEMBER_ID_NOT_EXISTS(HttpStatus.BAD_REQUEST, "member-bookmark_002", "책갈피가 등록되지 않은 회원입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
