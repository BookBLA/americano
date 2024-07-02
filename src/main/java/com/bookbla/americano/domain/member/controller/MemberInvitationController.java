package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.member.controller.dto.request.MemberInvitationEntryRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberInvitationResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberPostcardResponse;
import com.bookbla.americano.domain.member.service.MemberInvitationService;
import io.swagger.v3.oas.annotations.Parameter;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/members/me/invitation-code")
    public ResponseEntity<MemberPostcardResponse> entryInvitationCode(
            @Parameter(hidden = true) @User LoginUser loginUser,
            @RequestBody @Valid MemberInvitationEntryRequest request
    ) {
        MemberPostcardResponse response = memberInvitationService.entryInvitationCode(loginUser.getMemberId(), request);
        return ResponseEntity.ok(response);
    }

}
