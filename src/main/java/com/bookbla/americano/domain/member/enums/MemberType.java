package com.bookbla.americano.domain.member.enums;

public enum MemberType {
    APPLE,
    KAKAO,
    GOOGLE,
    ADMIN,
    ;

    public boolean isAdmin() {
        return this == ADMIN;
    }
}
