package com.bookbla.americano.domain.auth.controller;

import com.bookbla.americano.base.jwt.TokenProvider;
import com.bookbla.americano.domain.auth.controller.dto.request.LoginRequestDto;
import com.bookbla.americano.domain.auth.controller.dto.response.LoginResponseDto;
import com.bookbla.americano.domain.user.controller.dto.request.UserRegisterRequestDto;
import com.bookbla.americano.domain.user.controller.dto.response.UserRegisterResponseDto;
import com.bookbla.americano.domain.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final String TOKEN_HEADER_NAME = "Authorization";
    private static final String TOKEN_HEADER_PREFIX = "Bearer ";

    private final String tokenExpireTime;
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthController(@Value("${jwt.token-validity-in-seconds}") String tokenExpireTime,
                          UserService userService,
                          TokenProvider tokenProvider,
                          AuthenticationManagerBuilder authenticationManagerBuilder) {

        this.tokenExpireTime = tokenExpireTime;
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/signup")
    public UserRegisterResponseDto signup(@Valid @RequestBody UserRegisterRequestDto userRegisterRequestDto) {
        return userService.signup(userRegisterRequestDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication);

        return ResponseEntity.ok()
                .header(TOKEN_HEADER_NAME, TOKEN_HEADER_PREFIX + token)
                .body(
                        LoginResponseDto.builder()
                                .tokenExpireTime(Long.parseLong(tokenExpireTime))
                                .build()
                );

    }
}

