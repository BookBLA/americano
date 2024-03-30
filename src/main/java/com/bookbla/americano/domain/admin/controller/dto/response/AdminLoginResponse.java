package com.bookbla.americano.domain.admin.controller.dto.response;

import com.bookbla.americano.domain.admin.repository.entity.AdminSession;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class AdminLoginResponse {

    private final String sessionId;

    public static AdminLoginResponse from(AdminSession adminSession) {
        return new AdminLoginResponse(adminSession.getSessionId());
    }
}
