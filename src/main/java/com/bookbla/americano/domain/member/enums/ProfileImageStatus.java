package com.bookbla.americano.domain.member.enums;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.exception.MemberProfileException;
import java.util.Arrays;

public enum ProfileImageStatus {

    PENDING,
    DENIAL,
    DONE,
    ;

    public static ProfileImageStatus from(String name) {
        return Arrays.stream(values())
            .filter(it -> it.name().equalsIgnoreCase(name))
            .findFirst()
            .orElseThrow(() -> new BaseException(MemberProfileException.OKA_STATUS_NOT_VALID));
    }

    public boolean isDone() {
        return this == DONE;
    }
}
