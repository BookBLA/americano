package com.bookbla.americano.domain.member.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberReportExceptionType implements ExceptionType {

    SAME_MEMBER(HttpStatus.BAD_REQUEST, "member-report_001", "같은 유저는 신고할 수 없습니다."),
    ALREADY_MEMBER_REPORT(HttpStatus.BAD_REQUEST, "member-report_002", "이미 신고가 된 상태입니다"),
    NOT_FOUND_MEMBER_REPORT(HttpStatus.BAD_REQUEST, "member-report_003", "해당 memberReportId는 존재하지 않습니다."),
    NOT_SAME_MEMBER_REPORT(HttpStatus.BAD_REQUEST, "member-report_004", "해당 memberReportId와 현재 로그인 유저가 맞지 않습니다."),
    ALL_REPORT_FALSE(HttpStatus.BAD_REQUEST, "member-report_005", "모든 신고 항목이 FALSE 입니다."),
    ETC_CONTENTS_EMPTY(HttpStatus.BAD_REQUEST, "member-report_006", "기타 내용이 빈 칸입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
