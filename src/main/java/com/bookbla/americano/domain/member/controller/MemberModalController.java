package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.member.controller.dto.request.MemberInvitationRewardRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberOnboardingStatusRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberInvitationRewardResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberOnboardingStatusResponse;
import com.bookbla.americano.domain.member.service.MemberModalService;
import com.bookbla.americano.domain.school.service.InvitationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/member-modal")
@RequiredArgsConstructor
public class MemberModalController {

    private final MemberModalService memberModalService;
    private final InvitationService invitationService;

    @Operation(summary = "사용자 온모딩 모달 상태 조회 API")
    @GetMapping("/onboarding")
    public ResponseEntity<MemberOnboardingStatusResponse> getOnboardingStatus(
            @Parameter(hidden = true) @User LoginUser loginUser
    ) {
        return ResponseEntity.ok(memberModalService.getMemberOnboarding(loginUser.getMemberId()));
    }

    @Operation(summary = "사용자 온보딩 모달 상태 업데이트 API",
            description = "사용자의 온보딩 상태를 업데이트<br>"
                    + "memberOnboardingStatus : [HOME, LIBRARY]")
    @PostMapping("/onboarding")
    public ResponseEntity<MemberOnboardingStatusResponse> updateOnboardingStatus(
            @Parameter(hidden = true) @User LoginUser loginUser,
            @RequestBody @Valid MemberOnboardingStatusRequest memberOnboardingStatusRequest
    ) {
        return ResponseEntity.ok(memberModalService.updateMemberOnboarding(loginUser.getMemberId(), memberOnboardingStatusRequest.getOnboarding()));
    }

    @Operation(summary = "친구 초대 보상 상태 조회 API")
    @GetMapping("/invitation-reward")
    public ResponseEntity<MemberInvitationRewardResponse> getInvitationRewardStatus(
            @Parameter(hidden = true) @User LoginUser loginUser
    ) {
        return ResponseEntity.ok(invitationService.getInvitationRewardStatus(loginUser.getMemberId()));
    }

    @Operation(summary = "친구 초대 보상 상태 업데이트 API",
            description = "친구 초대 보상 상태를 업데이트<br>"
                    + "memberOnboardingStatus : [PENDING, BOOKMARK, COMPLETED]")
    @PostMapping("/invitation-reward")
    public ResponseEntity<MemberInvitationRewardResponse> updateInvitationRewardStatus(
            @Parameter(hidden = true) @User LoginUser loginUser,
            @RequestBody @Valid MemberInvitationRewardRequest request
    ) {
        return ResponseEntity.ok(invitationService.updateInvitationRewardStatus(loginUser.getMemberId(), request.getInvitationStatus()));
    }
}
