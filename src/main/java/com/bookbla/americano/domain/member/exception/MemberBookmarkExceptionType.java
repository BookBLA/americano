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
    ADMOB_COUNT_NOT_VALID(HttpStatus.BAD_REQUEST, "member-bookmark_003", "금일 애드몹 시청 횟수가 초과되었습니다"),
    ADMOB_TYPE_NOT_FOUND(HttpStatus.BAD_REQUEST, "member-bookmark_004", "존재하지 않는 애드몹 유형입니다"),
    INVALID_BOOKMARK_REWARD_COUNT(HttpStatus.BAD_REQUEST, "member-bookmark_005", "최초 도서 등록 책갈피 지급 개수를 초과했습니다"),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
