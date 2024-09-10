package com.bookbla.americano.base.jwt;


import com.bookbla.americano.base.exception.AuthExceptionType;
import com.bookbla.americano.base.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;

    // request가 다음 Layer로 전달하기 전에 미리 핸들링, true 반환시 다음으로 넘어감
    // https://www.baeldung.com/spring-mvc-handlerinterceptor
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        if (CorsUtils.isPreFlightRequest(request)) {
            return true;
        }
        if (hasNotAuthorization(request)) {
            throw new BaseException(AuthExceptionType.MISSING_AUTH_HEADER);
        }

        jwtProvider.validateToken(BearerTokenExtractor.extract(request));
        return true;
    }

    private boolean hasNotAuthorization(HttpServletRequest request) {
        return !StringUtils.hasText(request.getHeader(AUTHORIZATION));
    }
}
