package com.bookbla.americano.domain.auth.controller;

import com.bookbla.americano.base.jwt.JwtProvider;
import com.bookbla.americano.base.jwt.LoginUser;
import com.bookbla.americano.domain.auth.controller.dto.response.LoginResponseDto;
import com.bookbla.americano.domain.auth.service.AuthService;
import com.bookbla.americano.domain.member.Member;
import com.bookbla.americano.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final JwtProvider jwtProvider;
    private final AuthService authService;
    private final MemberService memberService;

    @PostMapping("/{loginType}")
    public ResponseEntity<LoginResponseDto> login(@LoginUser Long memberId) {
        // 찾은 Member 활용
        Member member = memberService.getMemberById(memberId);
//         기타 로직
         LoginResponseDto loginResponseDto = authService.login("1");

        return ResponseEntity.ok(loginResponseDto);
    }
}
