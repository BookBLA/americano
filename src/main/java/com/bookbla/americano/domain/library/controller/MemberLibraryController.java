package com.bookbla.americano.domain.library.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.library.service.MemberLibraryService;
import com.bookbla.americano.domain.library.controller.dto.MemberLibraryProfileReadResponse;
import com.bookbla.americano.domain.library.controller.dto.MemberTargetLibraryProfileReadResponse;
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
        var response = memberLibraryService.getLibraryProfile(loginUser.getMemberId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/target/{targetMemberId}")
    public ResponseEntity<MemberTargetLibraryProfileReadResponse> readMemberProfileByTarget(
        @Parameter(hidden = true) @User LoginUser loginUser, @PathVariable Long targetMemberId) {
        MemberTargetLibraryProfileReadResponse memberLibraryTargetProfileReadResponse = memberLibraryService.getTargetLibraryProfile(loginUser.getMemberId(), targetMemberId);
        return ResponseEntity.ok(memberLibraryTargetProfileReadResponse);
    }
}
