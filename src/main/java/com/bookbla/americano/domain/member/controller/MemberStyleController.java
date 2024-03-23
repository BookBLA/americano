package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.member.controller.dto.request.MemberStyleCreateRequest;
import com.bookbla.americano.domain.member.service.MemberStyleService;
import com.bookbla.americano.domain.member.controller.dto.response.MemberStyleResponse;
import com.bookbla.americano.domain.member.controller.dto.request.MemberStyleUpdateRequest;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members/styles")
public class MemberStyleController {

    private final MemberStyleService memberStyleService;

    @PostMapping
    public ResponseEntity<MemberStyleResponse> createMemberStyle(
            @User LoginUser loginUser,
            @RequestBody @Valid MemberStyleCreateRequest memberStyleCreateRequest) {
        MemberStyleResponse memberStyleResponse = memberStyleService.createMemberStyle(
                loginUser.getMemberId(), memberStyleCreateRequest);
        return ResponseEntity.created(URI.create(memberStyleResponse.getMemberStyleId().toString()))
                .body(memberStyleResponse);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberStyleResponse> readMemberStyle(@PathVariable Long memberId) {
        MemberStyleResponse memberStyleResponse = memberStyleService.readMemberStyle(memberId);
        return ResponseEntity.ok(memberStyleResponse);
    }

    @PutMapping
    public ResponseEntity<Void> updateMemberStyle(
            @User LoginUser loginUser,
            @RequestBody @Valid MemberStyleUpdateRequest memberStyleUpdateRequest) {
        memberStyleService.updateMemberStyle(loginUser.getMemberId(), memberStyleUpdateRequest);
        return ResponseEntity.noContent().build();
    }
}

