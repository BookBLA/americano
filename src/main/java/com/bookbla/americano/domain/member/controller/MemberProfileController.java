package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.jwt.LoginUser;
import com.bookbla.americano.domain.member.controller.dto.request.MemberProfileCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberProfileCreateResponse;
import com.bookbla.americano.domain.member.service.MemberProfileService;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member-profiles")
@RequiredArgsConstructor
public class MemberProfileController {

    private final MemberProfileService memberProfileService;

    @PostMapping
    public ResponseEntity<MemberProfileCreateResponse> createMemberProfile(
        @RequestBody @Valid MemberProfileCreateRequest memberProfileCreateRequest,
        @LoginUser Long memberId) {

        MemberProfileCreateResponse memberProfileCreateResponse =
            memberProfileService.createProfile(memberId, memberProfileCreateRequest);

        return ResponseEntity.created(URI.create("/member-profiles/" + memberProfileCreateResponse.getId()))
            .body(memberProfileCreateResponse);
    }

}
