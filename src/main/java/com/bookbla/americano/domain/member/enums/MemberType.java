package com.bookbla.americano.domain.member.enums;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import java.util.Arrays;


public enum MemberType {

    APPLE,
    KAKAO,
    GOOGLE,
    ADMIN,
    ;

    public static MemberType from(String other) {
        return Arrays.stream(values())
                .filter(type -> type.name().equalsIgnoreCase(other))
                .findFirst()
                .orElseThrow(() -> new BaseException(MemberExceptionType.MEMBER_TYPE_NOT_VALID));
    }

    public boolean isAdmin() {
        return this == ADMIN;
    }
}
