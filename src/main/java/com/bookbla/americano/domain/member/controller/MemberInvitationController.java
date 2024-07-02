package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.member.controller.dto.response.MemberInvitationResponse;
import com.bookbla.americano.domain.member.service.MemberInvitationService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberInvitationController {

    private final MemberInvitationService memberInvitationService;

    @GetMapping("/members/me/invitation-code")
    public ResponseEntity<MemberInvitationResponse> readInvitationCode(
            @Parameter(hidden = true) @User LoginUser loginUser
    ) {
        MemberInvitationResponse response = memberInvitationService.getInvitationCode(loginUser.getMemberId());
        return ResponseEntity.ok(response);
    }

}
