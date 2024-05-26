package com.bookbla.americano.domain.member.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberPostcardExceptionType implements ExceptionType {

    INVALID_POSTCARD_COUNTS(HttpStatus.BAD_REQUEST, "member-post-card_001", "엽서 개수가 부족합니다. 충전 시간을 확인해주세요"),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
