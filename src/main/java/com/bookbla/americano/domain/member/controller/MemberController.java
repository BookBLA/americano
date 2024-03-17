package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.jwt.LoginUser;
import com.bookbla.americano.domain.member.controller.dto.request.MemberUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberStatusResponse;
import com.bookbla.americano.domain.member.service.MemberService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<MemberResponse> readMember(@LoginUser Long memberId) {

        MemberResponse memberResponse = memberService.readMember(memberId);
        return ResponseEntity.ok(memberResponse);
    }

    @GetMapping("/statuses")
    public ResponseEntity<MemberStatusResponse> readMemberStatus(@LoginUser Long memberId) {

        MemberStatusResponse memberStatusResponse = memberService.readMemberStatus(memberId);
        return ResponseEntity.ok(memberStatusResponse);
    }

    @PutMapping
    public ResponseEntity<MemberResponse> updateMember(
        @RequestBody @Valid MemberUpdateRequest memberUpdateRequest,
        @LoginUser Long memberId) {

        MemberResponse memberResponse = memberService.updateMember(memberId, memberUpdateRequest);
        return ResponseEntity.ok(memberResponse);
    }

}
