package com.bookbla.americano.domain.school.repository.entity;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum InvitationStatus {

    NONE,
    COMPLETED,
    BOOKMARK,
    ;

    @JsonCreator
    public static InvitationStatus from(String name) {
        return Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new BaseException(MemberExceptionType.MEMBER_STATUS_NOT_VALID));
    }
}
