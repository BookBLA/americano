package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.jwt.LoginUser;
import com.bookbla.americano.domain.member.controller.dto.request.MemberStyleCreateRequest;
import com.bookbla.americano.domain.member.service.MemberStyleService;
import com.bookbla.americano.domain.member.controller.dto.response.MemberStyleResponse;
import com.bookbla.americano.domain.member.controller.dto.request.MemberStyleUpdateRequest;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members/styles")
public class MemberStyleController {

    private final MemberStyleService memberStyleService;

    @PostMapping
    public ResponseEntity<MemberStyleResponse> createMemberStyle(@LoginUser Long memberId, MemberStyleCreateRequest memberStyleCreateRequest) {
        MemberStyleResponse memberStyleResponse = memberStyleService.createMemberStyle(
                memberId, memberStyleCreateRequest);
        return ResponseEntity.created(URI.create(memberStyleResponse.getMemberStyleId().toString()))
                .body(memberStyleResponse);
    }

    @GetMapping
    public ResponseEntity<MemberStyleResponse> readMemberStyle(@LoginUser Long memberId) {
        MemberStyleResponse memberStyleResponse = memberStyleService.readMemberStyle(memberId);
        return ResponseEntity.ok(memberStyleResponse);
    }

    @PutMapping
    public ResponseEntity<Void> updateMemberStyle(@LoginUser Long memberId, MemberStyleUpdateRequest memberStyleUpdateRequest) {
        memberStyleService.updateMemberStyle(memberId, memberStyleUpdateRequest);
        return ResponseEntity.noContent().build();
    }
}
