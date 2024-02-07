package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.domain.member.controller.dto.StylesResponse;
import com.bookbla.americano.domain.member.service.MemberStyleService;
import com.bookbla.americano.domain.member.service.dto.MemberStyleResponse;
import com.bookbla.americano.domain.member.service.dto.MemberStyleUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberStyleService memberStyleService;

    @GetMapping("/styles")
    public ResponseEntity<StylesResponse> readStyles() {
        return ResponseEntity.ok(memberStyleService.readStyles());
    }

    // member 식별자를 어디서 받아올까...
    @GetMapping("/styles/{memberId}")
    public ResponseEntity<MemberStyleResponse> readMemberStyle(Long memberId) {
        MemberStyleResponse memberStyleResponse = memberStyleService.readMemberStyle(memberId);
        return ResponseEntity.ok(memberStyleResponse);
    }

    // member 식별자를 어디서 받아올까...
    @PutMapping("/styles")
    public ResponseEntity<Void> updateMemberStyle(Long memberId, MemberStyleUpdateRequest memberStyleUpdateRequest) {
        memberStyleService.updateMemberStyle(memberId, memberStyleUpdateRequest);
        return ResponseEntity.noContent().build();
    }

}
