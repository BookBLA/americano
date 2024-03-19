package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.jwt.LoginUser;
import com.bookbla.americano.domain.member.controller.dto.request.MemberStyleCreateRequest;
import com.bookbla.americano.domain.member.service.MemberStyleService;
import com.bookbla.americano.domain.member.controller.dto.response.MemberStyleResponse;
import com.bookbla.americano.domain.member.controller.dto.request.MemberStyleUpdateRequest;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members/styles")
public class MemberStyleController {

    private final MemberStyleService memberStyleService;

    @PostMapping
    public ResponseEntity<MemberStyleResponse> createMemberStyle(
            @LoginUser Long memberId,
            @RequestBody @Valid MemberStyleCreateRequest memberStyleCreateRequest) {
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

    @GetMapping("/target/{targetMemberId}")
    public ResponseEntity<MemberStyleResponse> readMemberStyle(@LoginUser Long memberId, @PathVariable Long targetMemberId) {
        MemberStyleResponse memberStyleResponse = memberStyleService.readMemberStyle(memberId, targetMemberId);
        return ResponseEntity.ok(memberStyleResponse);
    }

    @PutMapping
    public ResponseEntity<Void> updateMemberStyle(
            @LoginUser Long memberId,
            @RequestBody @Valid MemberStyleUpdateRequest memberStyleUpdateRequest) {
        memberStyleService.updateMemberStyle(memberId, memberStyleUpdateRequest);
        return ResponseEntity.noContent().build();
    }
}

