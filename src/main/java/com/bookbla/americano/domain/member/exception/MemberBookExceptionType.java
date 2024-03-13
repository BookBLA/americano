package com.bookbla.americano.domain.member.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberBookExceptionType implements ExceptionType {

    MEMBER_BOOK_NOT_FOUND(HttpStatus.FORBIDDEN, "member-book_001", "해당 도서를 등록한 회원이 아닙니다."),
    REVIEW_LENGTH_NOT_VALID(HttpStatus.BAD_REQUEST, "member-book_002", "리뷰는 최대 100자까지 등록 가능합니다."),
    MEMBER_BOOK_EXISTS(HttpStatus.BAD_REQUEST, "member-book_003", "해당 회원이 이미 등록한 도서입니다."),
    MAX_MEMBER_BOOK_COUNT(HttpStatus.BAD_REQUEST, "member-book_004", "회원당 도서는 최대 3권까지 등록할 수 있습니다.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
