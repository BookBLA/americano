package com.bookbla.americano.base.interceptor;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.admin.excpetion.AdminExceptionType;
import com.bookbla.americano.domain.admin.service.AdminAuthService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
@Component
public class AdminInterceptor implements HandlerInterceptor {

    private static final String SESSION = "SESSION";

    private final AdminAuthService adminAuthService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new BaseException(AdminExceptionType.ADMIN_AUTH_FAIL);
        }

        String uuid = (String) session.getAttribute(SESSION);
        adminAuthService.validateSession(uuid);
        return true;
    }
}
