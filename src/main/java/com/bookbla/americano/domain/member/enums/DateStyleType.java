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
public enum DateStyleType {

    HOME("집 데이트", "🏠실내 데이트를 선호해요"),
    OUTSIDE("야외 데이트", "🏟️야외 데이트를 선호해요"),
    ;

    private final String value;
    private final String detailValue;

    public static List<String> getValues() {
        return Arrays.stream(values())
                .map(it -> it.value)
                .collect(Collectors.toList());
    }

    public static DateStyleType from(String name) {
        return Arrays.stream(values())
                .filter(it -> it.value.equals(name))
                .findFirst()
                .orElseThrow(() -> new BaseException(MemberExceptionType.DATE_STYLE_NOT_VALID));
    }
}
