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
public enum SmokeType {

    SMOKE("흡연", "🚬흡연자"),
    NON_SMOKE("비흡연", "🚭비흡연자"),
    SOMETIMES("가끔", "🚬가끔 펴요"),
    ;

    private final String value;
    private final String detailValue;

    public static List<String> getValues() {
        return Arrays.stream(values())
                .map(it -> it.value)
                .collect(Collectors.toList());
    }

    public static SmokeType from(String other) {
        return Arrays.stream(values())
                .filter(it -> it.value.equalsIgnoreCase(other))
                .findFirst()
                .orElseThrow(() -> new BaseException(MemberExceptionType.SMOKE_TYPE_NOT_VALID));
    }
}
