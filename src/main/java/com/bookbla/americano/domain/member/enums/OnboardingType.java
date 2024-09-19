package com.bookbla.americano.domain.member.enums;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum OnboardingType {

    HOME,
    LIBRARY,
    INVALID
    ;

    @JsonCreator
    public static OnboardingType fromName(String name) {
        return Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new BaseException(MemberExceptionType.ONBOARDING_NOT_VALID));
    }
}
