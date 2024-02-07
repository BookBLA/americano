package com.bookbla.americano.domain.member.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DateStyle {

    HOME("집 데이트"),
    OUTSIDE("야외 데이트"),
    ;

    private final String value;

}
