package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.domain.member.controller.dto.request.MemberPostcardCountRequestDto;
import com.bookbla.americano.domain.member.service.MemberPostcardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member-postcards")
@RequiredArgsConstructor
public class MemberPostcardController {

    private final MemberPostcardService memberPostcardService;

    @GetMapping("/count")
    public ResponseEntity<Integer> memberPostcardCount(@ModelAttribute MemberPostcardCountRequestDto memberPostcardCountRequestDto) {
        Integer memberPostcardCount = memberPostcardService.getMemberPostcardCount(memberPostcardCountRequestDto);
        return ResponseEntity.ok(memberPostcardCount);
    }
}
