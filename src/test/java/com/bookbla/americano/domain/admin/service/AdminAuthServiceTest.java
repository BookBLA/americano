package com.bookbla.americano.domain.admin.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.admin.controller.dto.request.AdminLoginRequest;
import com.bookbla.americano.domain.admin.controller.dto.request.AdminLogoutRequest;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminLoginResponse;
import com.bookbla.americano.domain.admin.repository.AdminRepository;
import com.bookbla.americano.domain.admin.repository.AdminSessionRepository;
import com.bookbla.americano.domain.admin.repository.entity.Admin;
import com.bookbla.americano.domain.admin.repository.entity.AdminSession;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
class AdminAuthServiceTest {

    @Autowired
    private AdminAuthService adminAuthService;

    @Autowired
    private AdminSessionRepository adminSessionRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void 입력한_아이디에_해당하는_관리자가_없다면_예외가_발생한다() {
        // given
        AdminLoginRequest adminLoginRequest = new AdminLoginRequest("user", "password");

        // when, then
        assertThatThrownBy(() -> adminAuthService.login(adminLoginRequest))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("해당 사용자 id를 가진 관리자가 존재하지 않습니다");
    }

    @Test
    void 비밀번호를_최대_실패_가능_횟수_이상_틀리는_경우_예외가_발생한다() {
        // given
        Admin admin = Admin.builder()
                .userId("user")
                .password(passwordEncoder.encode("password"))
                .failCount(5)
                .build();
        adminRepository.save(admin);
        AdminLoginRequest adminLoginRequest = new AdminLoginRequest(admin.getUserId(), "password");

        // when, then
        assertThatThrownBy(() -> adminAuthService.login(adminLoginRequest))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("최대 비밀번호 입력 횟수를 초과했습니다");
    }

    @Test
    void 로그인에_실패한다면_예외가_발생한다() {
        // given
        Admin admin = Admin.builder()
                .userId("user")
                .password(passwordEncoder.encode("password"))
                .build();
        Admin savedAdmin = adminRepository.save(admin);
        AdminLoginRequest adminLoginRequest = new AdminLoginRequest(savedAdmin.getUserId(), "wrongPassword");

        // when, then
        assertThatThrownBy(() -> adminAuthService.login(adminLoginRequest))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("비밀번호가 일치하지 않습니다.");
    }

    @Test
    void 로그인에_성공하는_경우_계정의_비밀번호_실패_횟수가_초기화_된다() {
        // given
        Admin admin = Admin.builder()
                .userId("user")
                .password(passwordEncoder.encode("password"))
                .build();
        Admin savedAdmin = adminRepository.save(admin);
        AdminLoginRequest adminLoginRequest = new AdminLoginRequest(savedAdmin.getUserId(), "password");

        // when
        adminAuthService.login(adminLoginRequest);

        // then
        Admin findAdmin = adminRepository.findByUserId(admin.getUserId()).orElseThrow();
        assertThat(findAdmin.getFailCount()).isZero();
    }

    @Test
    void 로그인에_성공하는_경우_세션을_저장하고_세션의_UUID를_반환한다() {
        // given
        Admin admin = Admin.builder()
                .userId("user")
                .password(passwordEncoder.encode("password"))
                .build();
        Admin savedAdmin = adminRepository.save(admin);
        AdminLoginRequest adminLoginRequest = new AdminLoginRequest(savedAdmin.getUserId(), "password");

        // when
        AdminLoginResponse adminLoginResponse = adminAuthService.login(adminLoginRequest);

        // then
        Optional<AdminSession> findAdminSession = adminSessionRepository.findBySessionId(adminLoginResponse.getSessionId());
        assertThat(findAdminSession).isPresent();
    }

    @Test
    void 올바르지_않은_세션은_예외가_발생한다() {
        // when, then
        assertThatThrownBy(() -> adminAuthService.validateSession("wrongUUID"))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("올바르지 않은 세션입니다");
    }

    @Test
    void 세션이_만료되었다면_예외가_발생한다() {
        // given
        String uuid = UUID.randomUUID().toString();
        AdminSession adminSession = new AdminSession(1L, uuid, LocalDateTime.now().minusMinutes(1));
        adminSessionRepository.save(adminSession);

        // when, then
        assertThatThrownBy(() -> adminAuthService.validateSession(uuid))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("세션이 만료되었습니다");
    }

    @Test
    void 만료되지_않은_세션이라면_예외가_발생하지_않는다() {
        // given
        AdminSession adminSession = adminSessionRepository.save(new AdminSession());

        // when, then
        assertDoesNotThrow(() -> adminAuthService.validateSession(adminSession.getSessionId()));
    }

    @Test
    void 로그아웃시_세션을_파기한다() {
        // given
        AdminSession adminSession = adminSessionRepository.save(new AdminSession());
        AdminLogoutRequest adminLogoutRequest = new AdminLogoutRequest(adminSession.getSessionId());

        // when
        adminAuthService.logout(adminLogoutRequest);

        // then
        Optional<AdminSession> findAdminSession = adminSessionRepository.findBySessionId(adminSession.getSessionId());
        assertThat(findAdminSession).isEmpty();
    }

}
