package com.bookbla.americano.domain.member.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberMatchingExceptionType implements ExceptionType {

    MATCHING_DOESNT_EXIST(HttpStatus.NOT_FOUND, "member_matching_001", "매칭될 사용자가 존재하지 않습니다."),
    EXCEED_MAX_RECOMMENDATION(HttpStatus.BAD_REQUEST, "member_matching_002", "하루 최대 추천 회원 수를 초과하였습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
