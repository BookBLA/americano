package com.bookbla.americano.domain.member.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DrinkType {

    NONE("안마심"),
    ONCE_A_MONTH("월 1~2회"),
    ONCE_A_WEEK("주 1회"),
    OVER_ONCE_A_WEEK("주 1회 이상"),
    EVERYDAY("매일"),
    ;

    private final String value;

    public static List<String> getValues() {
        return Arrays.stream(values())
                .map(it -> it.value)
                .collect(Collectors.toList());
    }
}
