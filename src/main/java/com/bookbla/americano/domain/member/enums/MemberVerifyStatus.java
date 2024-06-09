package com.bookbla.americano.domain.member.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum MemberVerifyStatus {

    PENDING,
    SUCCESS,
    FAIL,
    ;

    public static List<String> getValues() {
        return Arrays.stream(values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}
