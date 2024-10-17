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
    MIN_MEMBER_REMOVABLE_BOOK_COUNT(HttpStatus.BAD_REQUEST, "member-book_004", "도서는 2권 이상 등록 후 삭제할 수 있습니다."),
    NOT_FOUND_BOOK_AUTHORS(HttpStatus.BAD_REQUEST, "member-book_005", "해당 회원이 등록한 책의 저자가 존재하지않습니다."),
    MEMBER_BOOK_EMPTY(HttpStatus.BAD_REQUEST, "member-book_006", "해당 회원의 도서가 등록되지 않았습니다."),
    BOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "member-book_007", "해당 도서가 존재하지 않습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
