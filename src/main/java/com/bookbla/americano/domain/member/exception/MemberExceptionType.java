package com.bookbla.americano.domain.member.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberExceptionType implements ExceptionType {

    EMAIL_NOT_VALID(HttpStatus.BAD_REQUEST, "member_001", "형식에 맞지 않는 이메일입니다"),
    MEMBER_TYPE_NOT_VALID(HttpStatus.NOT_FOUND, "member_002", "존재하지 않는 유저 타입입니다"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "member_003", "해당 식별자를 가진 회원이 존재하지 않습니다."),
    STYLE_NOT_REGISTERED(HttpStatus.BAD_REQUEST, "member_004", "스타일이 등록되지 않은 회원입니다."),
    MBTI_NOT_VALID(HttpStatus.BAD_REQUEST, "member_005", "유효하지 않은 MBTI입니다."),
    DATE_COST_TYPE_NOT_VALID(HttpStatus.BAD_REQUEST, "member_006", "유효하지 않은 데이트 비용 부담 방식입니다."),
    DRINK_TYPE_NOT_VALID(HttpStatus.BAD_REQUEST, "member_007", "유효하지 않은 음주 주기입니다."),
    SMOKE_TYPE_NOT_VALID(HttpStatus.BAD_REQUEST, "member_008", "유효하지 않은 흡연 주기입니다."),
    CONTAT_TYPE_NOT_VALID(HttpStatus.BAD_REQUEST, "member_009", "유효하지 않은 연락 주기입니다."),
    JUST_FRIEND_TYPE_NOT_VALID(HttpStatus.BAD_REQUEST, "member_010", "유효하지 않은 남-여사친 유형입니다."),
    DATE_STYLE_NOT_VALID(HttpStatus.BAD_REQUEST, "member_011", "유효하지 않은 데이트 스타일입니다."),
    ;


    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
