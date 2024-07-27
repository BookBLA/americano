package com.bookbla.americano.domain.notification.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.notification.controller.dto.request.MemberTokenCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberTokenCreateResponse;
import com.bookbla.americano.domain.notification.service.MemberTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members/tokens")
@RequiredArgsConstructor
public class MemberTokenController {

    private final MemberTokenService memberTokenService;

    @Operation(summary = "사용자의 EXPO 서버 토큰 생성 API",
        description = "사용자가 푸시 알림을 할 때 필요한 EXPO 서버의 토큰을 저장")
    @PostMapping
    public ResponseEntity<MemberTokenCreateResponse> createPushToken(
        @Parameter(hidden = true) @User LoginUser loginUser,
        @RequestBody @Valid MemberTokenCreateRequest memberTokenCreateRequest
    ) {
        MemberTokenCreateResponse memberTokenCreateResponse = memberTokenService.createPushToken(
            loginUser.getMemberId(), memberTokenCreateRequest);

        return ResponseEntity.ok(memberTokenCreateResponse);
    }

}
