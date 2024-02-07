package com.bookbla.americano.domain.member.enums;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
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

    public static DrinkType from(String name) {
        return Arrays.stream(values())
                .filter(it -> it.value.equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new BaseException(MemberExceptionType.DRINK_TYPE_NOT_VALID));
    }
}
