package com.bookbla.americano.domain.member.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberProfileExceptionType implements ExceptionType {

    GENDER_NOT_VALID(HttpStatus.BAD_REQUEST, "member_profile_01", "유효하지 않은 성별입니다."),
    OKA_STATUS_NOT_VALID(HttpStatus.BAD_REQUEST, "member_profile_02", "유효하지 않은 오픈 카톡방 상태입니다."),
    ALREADY_EXISTS_NICKNAME(HttpStatus.BAD_REQUEST, "member_profile_03", "이미 사용중인 닉네임입니다."),
    CONTAIN_BAD_WORDS(HttpStatus.BAD_REQUEST, "member_profile_04", "사용할 수 없는 단어가(비속어, 북블라 등) 포함되어있습니다."),
    PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND, "member_profile_05", "프로필이 등록되지 않은 회원입니다."),
    STUDENT_ID_NOT_VALID(HttpStatus.NOT_FOUND, "member_profile_06", "학생증이 인증되지 않은 회원입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
