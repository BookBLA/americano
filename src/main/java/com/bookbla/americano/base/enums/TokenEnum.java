package com.bookbla.americano.base.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenEnum {

    AUTHORITIES_KEY("auth"),
    TOKEN_PREFIX("Bearer "),
    AUTHORIZATION_HEADER("Authorization"),
    TOKEN_COOKIE_HEADER("accessToken");

    private final String name;
}
