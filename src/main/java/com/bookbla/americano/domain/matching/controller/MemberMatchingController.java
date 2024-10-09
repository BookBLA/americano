package com.bookbla.americano.domain.matching.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.matching.controller.dto.response.MemberIntroResponse;
import com.bookbla.americano.domain.matching.service.MemberMatchingService;
import datadog.trace.api.Trace;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원 매칭", description = "홈 화면 회원 매칭 관련 API")
@RestController
@RequestMapping("/members-match")
@RequiredArgsConstructor
public class MemberMatchingController {

    private final MemberMatchingService memberMatchingService;

    @Trace(resourceName = "매칭 회원 조회")
    @Operation(summary = "매칭 회원 조회")
    @GetMapping
    public ResponseEntity<MemberIntroResponse> getRecommendation(
            @Parameter(hidden = true) @User LoginUser loginUser) {
        return ResponseEntity.ok(memberMatchingService.getRecommendationMember(loginUser.getMemberId()));
    }

    @Trace(resourceName = "매칭 회원 새로고침")
    @Operation(summary = "매칭 회원 새로고침")
    @PostMapping("/refresh")
    public ResponseEntity<MemberIntroResponse> refreshMemberMatching(
            @Parameter(hidden = true) @User LoginUser loginUser) {
        return ResponseEntity.ok(memberMatchingService.refreshMemberMatching(loginUser.getMemberId()));
    }
}
