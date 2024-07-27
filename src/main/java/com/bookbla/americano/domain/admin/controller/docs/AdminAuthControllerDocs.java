package com.bookbla.americano.domain.admin.controller.docs;

import com.bookbla.americano.domain.admin.controller.dto.request.AdminLoginRequest;
import com.bookbla.americano.domain.admin.controller.dto.request.AdminLogoutRequest;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminLoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "관리자 인증", description = "관리자 로그인/로그아웃 API입니다.")
public interface AdminAuthControllerDocs {

    @Operation(summary = "관리자 로그인", description = "관리자 로그인을 합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "로그인에 성공하여 sessionId를 반환함"
    )
    @PostMapping
    ResponseEntity<AdminLoginResponse> login(@RequestBody @Valid AdminLoginRequest adminLoginRequest);

    @Operation(summary = "관리자 로그아웃", description = "관리자 로그아웃을 합니다.")
    @ApiResponse(
            responseCode = "204",
            description = "로그아웃하여 세션을 삭제"
    )
    @DeleteMapping
    ResponseEntity<Void> logout(@RequestBody @Valid AdminLogoutRequest adminLogoutRequest);
}
