package com.bookbla.americano.domain.member.enums;

import java.util.Arrays;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.exception.MemberBookmarkExceptionType;

public enum AdmobType {

    NEW_PERSON, FREE_BOOKMARK;

    public static AdmobType from(String value) {
        return Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new BaseException(MemberBookmarkExceptionType.ADMOB_TYPE_NOT_FOUND));
    }
}
