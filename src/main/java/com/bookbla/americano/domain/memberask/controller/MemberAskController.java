package com.bookbla.americano.domain.memberask.controller;


import com.bookbla.americano.domain.memberask.controller.dto.request.MemberAskCreateRequest;
import com.bookbla.americano.domain.memberask.controller.dto.request.MemberAskUpdateRequest;
import com.bookbla.americano.domain.memberask.controller.dto.response.MemberAskResponse;
import com.bookbla.americano.domain.memberask.service.MemberAskService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members/member-ask")
public class MemberAskController {

    private final MemberAskService memberAskService;

    @PostMapping
    public ResponseEntity<Void> createMemberAsk(
            Long memberId, MemberAskCreateRequest memberAskCreateRequest) {
        MemberAskResponse memberAskResponse = memberAskService.createMemberAsk(
                memberId, memberAskCreateRequest);
        return ResponseEntity.created(
                URI.create(memberAskResponse.getMemberAskResponseId().toString()))
                .build();
    }

    @GetMapping
    public ResponseEntity<MemberAskResponse> readMemberAsk(Long memberId) {
        MemberAskResponse memberAskResponse = memberAskService.readMemberAsk(memberId);
        return ResponseEntity.ok(memberAskResponse);
    }

    @PutMapping
    public ResponseEntity<Void> updateMemberAsk(
            Long memberId,MemberAskUpdateRequest memberAskUpdateRequest) {
        memberAskService.updateMemberAsk(memberId, memberAskUpdateRequest);
        return ResponseEntity.noContent().build();
    }
}
