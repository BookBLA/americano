package com.bookbla.americano.domain.member.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberBlockExceptionType implements ExceptionType {

    SAME_MEMBER(HttpStatus.BAD_REQUEST, "member-block_001", "같은 유저는 차단할 수 없습니다."),
    ALREADY_MEMBER_BLOCK(HttpStatus.BAD_REQUEST, "member-block_002", "이미 차단이 된 상태입니다"),
    NOT_FOUND_MEMBER_BLOCK(HttpStatus.BAD_REQUEST, "member-block_003", "해당 memberBlockId는 존재하지 않습니다."),
    NOT_SAME_MEMBER_BLOCK(HttpStatus.BAD_REQUEST, "member-block_004", "해당 memberBlockId와 현재 로그인 유저가 맞지 않습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
