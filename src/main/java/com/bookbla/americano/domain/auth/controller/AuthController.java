package com.bookbla.americano.domain.auth.controller;

import com.bookbla.americano.domain.auth.controller.dto.request.LoginRequestDto;
import com.bookbla.americano.domain.auth.controller.dto.response.LoginResponseDto;
import com.bookbla.americano.domain.auth.service.impl.AuthServiceImpl;
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

    private final AuthServiceImpl authService;

    @PostMapping("/login/{oAuthType}")
    public ResponseEntity<LoginResponseDto> login(
            @RequestBody LoginRequestDto loginRequestDto,
            @PathVariable String oAuthType
    ) {
        LoginResponseDto loginResponseDto = authService.login(loginRequestDto, oAuthType);
        return ResponseEntity.ok(loginResponseDto);

    }
}
