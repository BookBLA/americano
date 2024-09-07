package com.bookbla.americano.domain.member.enums;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.exception.MemberProfileExceptionType;
import java.util.Arrays;

public enum Gender {
    MALE,
    FEMALE
    ;

    public static Gender from(String name) {
        return Arrays.stream(values())
            .filter(it -> it.name().equalsIgnoreCase(name))
            .findFirst()
            .orElseThrow(() -> new BaseException(MemberProfileExceptionType.GENDER_NOT_VALID));
    }

}
