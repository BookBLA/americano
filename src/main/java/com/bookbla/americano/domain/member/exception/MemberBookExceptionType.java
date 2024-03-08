package com.bookbla.americano.domain.member.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberBookExceptionType implements ExceptionType {

    MEMBER_BOOK_NOT_FOUND(HttpStatus.FORBIDDEN, "member-book_001", "해당 도서를 등록한 회원이 아닙니다."),
    REVIEW_LENGTH_NOT_VALID(HttpStatus.BAD_REQUEST, "member-book_002", "리뷰는 최대 150자까지 등록 가능합니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
