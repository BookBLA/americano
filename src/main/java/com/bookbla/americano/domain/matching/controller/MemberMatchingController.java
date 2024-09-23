package com.bookbla.americano.domain.matching.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.matching.controller.dto.request.RejectMemberRequest;
import com.bookbla.americano.domain.matching.controller.dto.response.MemberIntroResponse;
import com.bookbla.americano.domain.matching.service.MemberMatchingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Tag(name="회원 매칭" , description = "홈 화면 회원 매칭 관련 API")
@RestController
@RequestMapping("/members-match")
@RequiredArgsConstructor
public class MemberMatchingController {

    private final MemberMatchingService memberMatchingService;

    @Operation(summary = "매칭 회원 조회")
    @GetMapping
    public ResponseEntity<List<MemberIntroResponse>> getRecommendation(
            @Parameter(hidden = true) @User LoginUser loginUser) {
        return ResponseEntity.ok(memberMatchingService.getRecommendationList(loginUser.getMemberId()));
    }

    @Operation(summary = "매칭 회원 거절")
    @GetMapping("/reject")
    public ResponseEntity<Void> rejectMemberMatching(
            @Parameter(hidden = true) @User LoginUser loginUser,
            @RequestBody @Valid RejectMemberRequest request) {
        memberMatchingService.rejectMemberMatching(loginUser.getMemberId(),
                request.getRejectedMemberId(), request.getRejectedMemberBookId());
        return ResponseEntity.ok().build();
    }
}
