package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.jwt.LoginUser;
import com.bookbla.americano.domain.member.controller.dto.response.MemberLibraryProfileReadResponse;
import com.bookbla.americano.domain.member.service.MemberLibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/library")
public class MemberLibraryController {

    private final MemberLibraryService memberLibraryService;

    @GetMapping
    public ResponseEntity<MemberLibraryProfileReadResponse> readMemberProfile(@LoginUser Long memberId) {
        MemberLibraryProfileReadResponse memberLibraryProfileReadResponse = memberLibraryService.getLibraryProfile(memberId);
        return ResponseEntity.ok(memberLibraryProfileReadResponse);
    }

    @GetMapping("/target/{targetMemberId}")
    public ResponseEntity<MemberLibraryProfileReadResponse> readMemberProfileByTarget(@LoginUser Long memberId, @PathVariable Long targetMemberId) {
        MemberLibraryProfileReadResponse memberLibraryProfileReadResponse = memberLibraryService.getLibraryProfile(memberId, targetMemberId);
        return ResponseEntity.ok(memberLibraryProfileReadResponse);
    }
}
