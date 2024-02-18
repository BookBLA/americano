package com.bookbla.americano.domain.member.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// DateStyle
@Getter
@RequiredArgsConstructor
public enum DateStyle {

    HOME("집 데이트"),
    OUTSIDE("야외 데이트"),
    ;

    private final String value;

}
