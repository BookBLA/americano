package com.bookbla.americano.domain.member.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberProfileExceptionType implements ExceptionType {

    PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND, "member-profile_001", "프로필이 등록되지 않은 회원입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
