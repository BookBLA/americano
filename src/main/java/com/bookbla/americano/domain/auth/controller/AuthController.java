package com.bookbla.americano.domain.auth.controller;

import com.bookbla.americano.domain.auth.controller.dto.request.LoginRequestDto;
import com.bookbla.americano.domain.auth.controller.dto.response.LoginResponseDto;
import com.bookbla.americano.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {


    private final AuthService authService;

    @PostMapping("/login/{type}")
    public ResponseEntity<LoginResponseDto> login(
            @RequestBody LoginRequestDto loginRequestDto,
            @RequestParam String oAuthType
    ) {
        LoginResponseDto loginResponseDto = authService.login(loginRequestDto, oAuthType);
        return ResponseEntity.ok(loginResponseDto);

//    @PostMapping("")
//    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
//        return ResponseEntity.ok()
//            .header(AUTHORIZATION, jwtProvider.createToken(loginRequestDto.getEmail()))
//            .body(LoginResponseDto.builder()
//                .message("인증 성공")
//                .build()
//            );
//    }
//
//    @GetMapping("/oauth2/kakao")
//    public ResponseEntity<LoginResponseDto> kakaoLogin(@RequestParam("code") String code) {
//
//        Long memberId = authService.kakaoLogin(code);
//        System.out.println("memberId : " + memberId);
//
//        return ResponseEntity.ok()
//            .header(AUTHORIZATION, jwtProvider.createToken(memberId.toString()))
//            .body(LoginResponseDto.builder()
//                .message("인증 성공")
//                .build()
//            );
//    }

    }
}
