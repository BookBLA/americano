package com.bookbla.americano.domain.member.enums;

import java.util.Arrays;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProfileImageType {

    MALE_FIRST(1, "url"),
    MALE_SECOND(2, "url"),
    MALE_THIRD(3, "url"),
    MALE_FOURTH(4, "url"),

    FEMALE_FIRST(5, "url"),
    FEMALE_SECOND(6, "url"),
    FEMALE_THIRD(7, "url"),
    FEMALE_FOURTH(8, "url"),
    ;

    private final int id;
    private final String imageUrl;

    public static ProfileImageType fromId(int id) {
        return Arrays.stream(values())
                .filter(it -> it.id == id)
                .findFirst()
                .orElseThrow(() -> new BaseException(MemberExceptionType.PROFILE_TYPE_NOT_FOUND));
    }
}
