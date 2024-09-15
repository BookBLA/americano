package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.member.controller.dto.response.MemberIntroResponse;
import com.bookbla.americano.domain.member.service.MemberMatchingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/members-match")
@RequiredArgsConstructor
public class MemberMatchController {

    private final MemberMatchingService memberMatchingService;

    @Operation(summary = "홈 화면 - 매칭 회원 조회")
    @GetMapping
    public ResponseEntity<MemberIntroResponse> getRecommendation(
            @RequestParam @Parameter(hidden = true) @User LoginUser loginUser,
            @RequestParam int index) {

        List<MemberIntroResponse> recommendations = memberMatchingService.getRecommendationList(loginUser.getMemberId());

        if (index >= 0 && index <= recommendations.size()) {
            MemberIntroResponse memberIntroResponse = recommendations.get(index);
            return ResponseEntity.ok(memberIntroResponse);
        } else {
            throw new IllegalArgumentException("x");
        }
    }
}
