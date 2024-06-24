package com.bookbla.americano.domain.memberask.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.memberask.controller.dto.request.MemberReplyUpdateRequest;
import com.bookbla.americano.domain.memberask.controller.dto.response.MemberReplyResponse;
import com.bookbla.americano.domain.memberask.service.MemberReplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members/member-reply")
public class MemberReplyController {

    private final MemberReplyService memberReplyService;

    @Operation(summary = "개인 질문 답변 조회", description = "사용자가 보낸 엽서의 개인 질문 답변 조회")
    @GetMapping("/{postcardId}")
    public ResponseEntity<MemberReplyResponse> readMemberReply(
            @Parameter(hidden = true) @User LoginUser loginUser, @PathVariable Long postcardId) {
        MemberReplyResponse memberReplyResponse =
                memberReplyService.readMemberReply(loginUser.getMemberId(), postcardId);
        return ResponseEntity.ok(memberReplyResponse);
    }

    @Operation(summary = "개인 질문 답변 수정", description = "사용자가 상대방의 응답을 기다리는 엽서(PENDING 상태)의 개인 질문 답변 수정")
    @PutMapping
    public ResponseEntity<Void> updateMemberReply(
            @Parameter(hidden = true) @User LoginUser loginUser,
            @RequestBody @Valid MemberReplyUpdateRequest memberReplyUpdateRequest) {
        memberReplyService.updateMemberReply(loginUser.getMemberId(), memberReplyUpdateRequest);
        return ResponseEntity.noContent().build();
    }
}
