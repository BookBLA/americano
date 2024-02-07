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

    @GetMapping
    public ResponseEntity<StylesResponse> readStyles() {
        return ResponseEntity.ok(memberStyleService.readStyles());
    }

}
