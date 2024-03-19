package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.jwt.LoginUser;
import com.bookbla.americano.domain.member.controller.dto.response.MemberLibraryProfileReadResponse;
import com.bookbla.americano.domain.member.service.MemberLibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/library")
public class MemberLibraryController {

    private final MemberLibraryService memberLibraryService;

    @GetMapping
    public ResponseEntity<MemberLibraryProfileReadResponse> readMemberProfile(@LoginUser Long memberId) {
        MemberLibraryProfileReadResponse memberLibraryProfileReadResponse = memberLibraryService.getLibraryProfile(memberId);
        return ResponseEntity.ok(memberLibraryProfileReadResponse);
    }
}
