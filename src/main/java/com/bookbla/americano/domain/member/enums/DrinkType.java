package com.bookbla.americano.domain.member.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DrinkType {

    NONE("안마심"),
    ONCE_A_MONTH("월 1~2회"),
    ONCE_A_WEEK("주 1회"),
    TWICE_A_WEEK("주 2회이상"),
    EVERYDAY("매일"),
    ;

    private final String value;
}
