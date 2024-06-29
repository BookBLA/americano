package com.bookbla.americano.domain.member.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberExceptionType implements ExceptionType {

    MEMBER_STATUS_NOT_VALID(HttpStatus.NOT_FOUND, "member_001", "유효하지 않은 유저 상태입니다."),
    MEMBER_TYPE_NOT_VALID(HttpStatus.NOT_FOUND, "member_002", "존재하지 않는 유저 타입입니다"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "member_003", "해당 식별자를 가진 회원이 존재하지 않습니다."),
    STYLE_NOT_REGISTERED(HttpStatus.BAD_REQUEST, "member_004", "스타일이 등록되지 않은 회원입니다."),
    MBTI_NOT_VALID(HttpStatus.BAD_REQUEST, "member_005", "유효하지 않은 MBTI입니다."),
    DATE_COST_TYPE_NOT_VALID(HttpStatus.BAD_REQUEST, "member_006", "유효하지 않은 데이트 비용 부담 방식입니다."),
    DRINK_TYPE_NOT_VALID(HttpStatus.BAD_REQUEST, "member_007", "유효하지 않은 음주 주기입니다."),
    SMOKE_TYPE_NOT_VALID(HttpStatus.BAD_REQUEST, "member_008", "유효하지 않은 흡연 주기입니다."),
    CONTACT_TYPE_NOT_VALID(HttpStatus.BAD_REQUEST, "member_009", "유효하지 않은 연락 주기입니다."),
    JUST_FRIEND_TYPE_NOT_VALID(HttpStatus.BAD_REQUEST, "member_010", "유효하지 않은 남-여사친 유형입니다."),
    DATE_STYLE_NOT_VALID(HttpStatus.BAD_REQUEST, "member_011", "유효하지 않은 데이트 스타일입니다."),
    EMPTY_MEMBER_BOOK(HttpStatus.NOT_FOUND, "4003", "사용자가 관심으로 등록한 책이 없습니다."),
    EMPTY_MEMBER_POSTCARD_INFO(HttpStatus.NOT_FOUND, "4004", "사용자의 엽서 개수 정보가 없습니다."),
    GENDER_NOT_VALID(HttpStatus.BAD_REQUEST, "member_012", "유효하지 않은 성별입니다."),
    AUTH_NOT_REGISTERED(HttpStatus.BAD_REQUEST, "member_013", "인증이 등록되지 않은 회원입니다."),
    PROFILE_NOT_REGISTERED(HttpStatus.BAD_REQUEST, "member_014", "프로필이 등록되지 않은 회원입니다."),
    STYLE_ALREADY_REGISTERD(HttpStatus.BAD_REQUEST, "member_015", "스타일이 이미 등록된 회원입니다."),
    ALREADY_MEMBER_DELETED(HttpStatus.BAD_REQUEST, "member_016", "회원이 이미 탈퇴 상태입니다."),
    MEMBER_STATUS_LOG_NOT_FOUND(HttpStatus.BAD_REQUEST, "member_017", "저장된 상태 변경 로그가 없습니다."),
    HEIGHT_NOT_FOUND(HttpStatus.NOT_FOUND, "member_018", "유효하지 않은 키 범위입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
