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

    NONE("X", "❌술을 안마셔요"),
    ONCE_A_MONTH("월 1~2회", "🥂술은 월 1~2회 정도 마셔요"),
    ONCE_A_WEEK("주 1회", "🍺술은 주 1회 정도 마셔요"),
    OVER_TWICE_A_WEEK("주 2회 이상", "🍻술은 주 2회 이상 마셔요"),
    EVERYDAY("매일", "🥃술은 매일 마셔요"),
    ;

    private final String value;
    private final String detailValue;

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
