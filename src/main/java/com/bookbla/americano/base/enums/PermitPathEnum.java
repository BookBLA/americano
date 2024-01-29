package com.bookbla.americano.base.enums;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public enum PermitPathEnum {
    H2_DATABASE_URI("/h2-console/**"),
    API_DOCS_URI("/api-docs/**"),
    SWAGGER_UI_URI("/swagger-ui/**"),
    ACTUATOR_URI("/actuator/**"),
    FAVICON_URI("/favicon.ico"),
    HELLO_URI("/api/user/hello"),
    REDIRECT_URI("/api/user/test-redirect"),
    AUTH_URI("/api/auth/**"),
    ALL_URI("/**"); //TODO 추후 제거

    private final String uri;

    public static RequestMatcher[] getPermitUriList() {
        return Arrays.stream(PermitPathEnum.values())
                .map(x -> new AntPathRequestMatcher(x.uri))
                .toArray(RequestMatcher[]::new);
    }
}
