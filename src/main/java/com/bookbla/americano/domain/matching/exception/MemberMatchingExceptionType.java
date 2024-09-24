package com.bookbla.americano.domain.matching.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberMatchingExceptionType implements ExceptionType {

    MATCHING_MEMBER_DOESNT_EXIST(HttpStatus.NOT_FOUND, "member_matching_001", "매칭될 사용자가 존재하지 않습니다."),
    NOT_FOUND_MATCHING(HttpStatus.NOT_FOUND, "member_matching_002", "매칭이 존재하지 않습니다."),
    EXCEED_MAX_RECOMMENDATION(HttpStatus.BAD_REQUEST, "member_matching_003", "하루 최대 추천 회원 수를 초과하였습니다."),
    ALREADY_MATCHED(HttpStatus.BAD_REQUEST, "member_matching_004", "이미 매칭된 회원입니다."),
    NOT_FOUND_MEMBER_BOOK(HttpStatus.NOT_FOUND, "member_matching_005", "회원의 책 정보가 존재하지 않습니다."),
    NOT_FOUND_MATCHED(HttpStatus.NOT_FOUND, "member_matching_006", "필터링된 회원이 존재하지 않습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
