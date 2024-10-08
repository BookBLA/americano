package com.bookbla.americano.domain.admin.controller;

import com.bookbla.americano.domain.admin.controller.docs.AdminAuthControllerDocs;
import com.bookbla.americano.domain.admin.controller.dto.request.AdminLoginRequest;
import com.bookbla.americano.domain.admin.controller.dto.request.AdminLogoutRequest;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminLoginResponse;
import com.bookbla.americano.domain.admin.service.AdminAuthService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin/auth")
@RequiredArgsConstructor
@RestController
public class AdminAuthController implements AdminAuthControllerDocs {

    private final AdminAuthService adminAuthService;

    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponse> login(@RequestBody @Valid AdminLoginRequest adminLoginRequest) {
        AdminLoginResponse adminLoginResponse = adminAuthService.login(adminLoginRequest);
        return ResponseEntity.ok(adminLoginResponse);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody @Valid AdminLogoutRequest adminLogoutRequest) {
        adminAuthService.logout(adminLogoutRequest);
        return ResponseEntity.noContent().build();
    }
}
