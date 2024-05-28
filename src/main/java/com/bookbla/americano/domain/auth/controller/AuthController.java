package com.bookbla.americano.domain.auth.controller;

import com.bookbla.americano.domain.auth.controller.dto.request.LoginRequest;
import com.bookbla.americano.domain.auth.controller.dto.request.RejoinRequest;
import com.bookbla.americano.domain.auth.controller.dto.response.LoginResponse;
import com.bookbla.americano.domain.auth.controller.dto.response.RejoinResponse;
import com.bookbla.americano.domain.auth.service.AuthService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login/{oAuthType}")
    public ResponseEntity<LoginResponse> login(
            @RequestBody @Valid LoginRequest loginRequest,
            @PathVariable String oAuthType
    ) {
        LoginResponse loginResponse = authService.login(loginRequest, oAuthType);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/rejoin/{oAuthType}")
    public ResponseEntity<RejoinResponse> rejoin(
        @RequestBody @Valid RejoinRequest rejoinRequest,
        @PathVariable String oAuthType
    ) {
        RejoinResponse rejoinResponse = authService.rejoin(rejoinRequest, oAuthType);
        return ResponseEntity.ok(rejoinResponse);
    }
}
