package com.bookbla.americano.domain.auth.controller;

import com.bookbla.americano.domain.auth.controller.dto.request.LoginRequestDto;
import com.bookbla.americano.domain.auth.controller.dto.request.RejoinRequest;
import com.bookbla.americano.domain.auth.controller.dto.response.LoginResponseDto;
import com.bookbla.americano.domain.auth.controller.dto.response.RejoinResponse;
import com.bookbla.americano.domain.auth.service.AuthService;
import com.bookbla.americano.domain.auth.service.impl.AuthServiceImpl;
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
    public ResponseEntity<LoginResponseDto> login(
            @RequestBody @Valid LoginRequestDto loginRequestDto,
            @PathVariable String oAuthType
    ) {
        LoginResponseDto loginResponseDto = authService.login(loginRequestDto, oAuthType);
        return ResponseEntity.ok(loginResponseDto);
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
