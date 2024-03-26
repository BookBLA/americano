package com.bookbla.americano.domain.member.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberProfileException implements ExceptionType {

    GENDER_NOT_VALID(HttpStatus.BAD_REQUEST, "member_profile_01", "유효하지 않은 성별입니다."),
    OKA_STATUS_NOT_VALID(HttpStatus.BAD_REQUEST, "member_profile_02", "유효하지 않은 오픈 카톡방 상태입니다."),
    NOT_FOUND_ID(HttpStatus.NOT_FOUND, "member-profile_03", "해당 식별자를 가진 회원 프로필이 존재하지 않습니다");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
