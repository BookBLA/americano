package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.member.controller.dto.response.MemberLibraryProfileReadResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberTargetLibraryProfileReadResponse;
import com.bookbla.americano.domain.member.service.MemberLibraryService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members/library")
public class MemberLibraryController {

    private final MemberLibraryService memberLibraryService;

    @GetMapping
    public ResponseEntity<MemberLibraryProfileReadResponse> readMemberProfile(
        @Parameter(hidden = true) @User LoginUser loginUser) {
        MemberLibraryProfileReadResponse memberLibraryProfileReadResponse = memberLibraryService.getLibraryProfile(
loginUser.getMemberId());
        return ResponseEntity.ok(memberLibraryProfileReadResponse);
    }

    @GetMapping("/target/{targetMemberId}")
    public ResponseEntity<MemberTargetLibraryProfileReadResponse> readMemberProfileByTarget(
        @Parameter(hidden = true) @User LoginUser loginUser, @PathVariable Long targetMemberId) {
        MemberTargetLibraryProfileReadResponse memberLibraryTargetProfileReadResponse = memberLibraryService.getTargetLibraryProfile(
            loginUser.getMemberId(), targetMemberId);
        return ResponseEntity.ok(memberLibraryTargetProfileReadResponse);
    }
}
