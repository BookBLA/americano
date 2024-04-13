package com.bookbla.americano.domain.member.enums;

import java.util.Arrays;

public enum MemberVerifyStatus {

    PENDING, SUCCESS, FAIL;

    public static MemberVerifyStatus from(String name) {
        return Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(IllegalAccessError::new);
    }
}
