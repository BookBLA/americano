package com.bookbla.americano.domain.admin.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.admin.controller.dto.request.AdminLoginRequest;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminLoginResponse;
import com.bookbla.americano.domain.admin.excpetion.AdminExceptionType;
import com.bookbla.americano.domain.admin.repository.AdminRepository;
import com.bookbla.americano.domain.admin.repository.AdminSessionRepository;
import com.bookbla.americano.domain.admin.repository.entity.Admin;
import com.bookbla.americano.domain.admin.repository.entity.AdminSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class AdminAuthService {

    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;
    private final AdminSessionRepository adminSessionRepository;

    public AdminLoginResponse login(AdminLoginRequest adminLoginRequest) {
        Admin admin = adminRepository.findByUserId(adminLoginRequest.getId())
                .orElseThrow(() -> new BaseException(AdminExceptionType.USER_ID_NOT_FOUND));
        admin.validateFailCount();

        if (admin.isDifferentPassword(passwordEncoder, adminLoginRequest.getPassword())) {
            admin.increaseFailCount();
            throw new BaseException(AdminExceptionType.NOT_MATCHED_PASSWORD);
        }

        admin.resetFailCount();
        AdminSession adminSession = adminSessionRepository.save(new AdminSession());
        return new AdminLoginResponse(adminSession.getSessionId());
    }

    public void validateSession(String uuid) {
        AdminSession adminSession = adminSessionRepository.findBySessionId(uuid)
                .orElseThrow(() -> new BaseException(AdminExceptionType.SESSION_NOT_FOUND));
        adminSession.validateExpired();
    }
}
