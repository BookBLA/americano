package com.bookbla.americano.domain.admin.controller;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.admin.excpetion.AdminExceptionType;
import com.bookbla.americano.domain.admin.service.AdminAuthService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
@Component
public class AdminInterceptor implements HandlerInterceptor {

    private static final String ADMIN_AUTH = "xxx-three-idiots-xxx";

    private final AdminAuthService adminAuthService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (CorsUtils.isPreFlightRequest(request)) {
            return true;
        }

        String sessionId = request.getHeader(ADMIN_AUTH);
        if (sessionId == null) {
            throw new BaseException(AdminExceptionType.ADMIN_AUTH_FAIL);
        }

        adminAuthService.validateSession(sessionId);
        return true;
    }
}
