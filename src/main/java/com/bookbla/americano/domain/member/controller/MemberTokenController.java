package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.member.controller.dto.request.MemberTokenCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberTokenCreateResponse;
import com.bookbla.americano.domain.member.service.MemberTokenService;
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

    @PostMapping("/tokens")
    public ResponseEntity<MemberTokenCreateResponse> createPushToken(
        @Parameter(hidden = true) @User LoginUser loginUser,
        @RequestBody @Valid MemberTokenCreateRequest memberTokenCreateRequest
    ) {
        MemberTokenCreateResponse memberTokenCreateResponse = memberTokenService.createPushToken(
            loginUser.getMemberId(), memberTokenCreateRequest);

        return ResponseEntity.ok(memberTokenCreateResponse);
    }

}
