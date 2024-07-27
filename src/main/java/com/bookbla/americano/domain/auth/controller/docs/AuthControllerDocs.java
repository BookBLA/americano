package com.bookbla.americano.domain.auth.controller.docs;

import com.bookbla.americano.domain.auth.controller.dto.request.LoginRequest;
import com.bookbla.americano.domain.auth.controller.dto.response.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthControllerDocs {

    @Operation(summary = "OAuth 로그인 API", description = "oAuthType -> [카카오 로그인 = kakao], [애플 로그인 = apple]")
    @ApiResponse(
            responseCode = "200",
            description = "인가코드를 받아 토큰을 반환합니다"
    )
    @PostMapping
    ResponseEntity<LoginResponse> login(
            @RequestBody @Valid LoginRequest loginRequest,
            @PathVariable String oAuthType
    );

}
