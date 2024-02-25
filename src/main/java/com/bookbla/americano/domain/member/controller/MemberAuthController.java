package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.jwt.LoginUser;
import com.bookbla.americano.domain.member.controller.dto.request.MemberAuthUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberAuthUpdateResponse;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.service.MemberAuthService;
import com.bookbla.americano.domain.member.service.MemberService;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member-auths")
@RequiredArgsConstructor
public class MemberAuthController {

    private final MemberAuthService memberAuthService;

    @PutMapping
    public ResponseEntity<MemberAuthUpdateResponse> updateMemberAuth(
        @RequestBody @Valid MemberAuthUpdateRequest memberAuthUpdateRequest,
        @LoginUser Long memberId) {

        MemberAuthUpdateResponse memberAuthUpdateResponse =
            memberAuthService.updateMemberAuth(memberId, memberAuthUpdateRequest.getPhoneNumber(),
                memberAuthUpdateRequest.getStudentIdImageUrl());

        return ResponseEntity.created(URI.create("/member-auths/" + memberAuthUpdateResponse.getId()))
            .body(memberAuthUpdateResponse);
    }
}
