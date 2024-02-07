package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.domain.member.controller.dto.StylesResponse;
import com.bookbla.americano.domain.member.service.MemberStyleService;
import com.bookbla.americano.domain.member.service.dto.MemberStyleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

}
