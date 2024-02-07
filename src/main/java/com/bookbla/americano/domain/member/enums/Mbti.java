package com.bookbla.americano.domain.member.enums;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import java.util.Arrays;

public enum Mbti {

    INTP,
    INTJ,
    INFP,
    INFJ,
    ISTP,
    ISTJ,
    ISFP,
    ISFJ,
    ENTP,
    ENTJ,
    ENFP,
    ENFJ,
    ESTP,
    ESTJ,
    ESFP,
    ESFJ;

    public static Mbti from(String name) {
        return Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new BaseException(MemberExceptionType.MBTI_NOT_VALID));
    }
}
