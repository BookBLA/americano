package com.bookbla.americano.domain.member.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SmokeType {

    SMOKE("흡연"),
    NON_SMOKE("비흡연"),
    SOMETIMES("가끔"),
    ;

    private final String value;

    public static List<String> getValues() {
        return Arrays.stream(values())
                .map(it -> it.value)
                .collect(Collectors.toList());
    }

}
