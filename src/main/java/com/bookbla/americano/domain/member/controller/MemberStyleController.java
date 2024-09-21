package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.member.controller.dto.request.MemberStyleCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberStyleUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberStyleResponse;
import com.bookbla.americano.domain.member.service.MemberStyleService;
import io.swagger.v3.oas.annotations.Parameter;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members/styles")
public class MemberStyleController {

    private final MemberStyleService memberStyleService;

    @PostMapping
    public ResponseEntity<MemberStyleResponse> createMemberStyle(
            @Parameter(hidden = true) @User LoginUser loginUser,
            @RequestBody @Valid MemberStyleCreateRequest memberStyleCreateRequest
    ) {
        MemberStyleResponse memberStyleResponse = memberStyleService.createMemberStyle(loginUser.getMemberId(), memberStyleCreateRequest);
        return ResponseEntity.ok(memberStyleResponse);
    }

    @GetMapping
    public ResponseEntity<MemberStyleResponse> readMemberStyle(
            @Parameter(hidden = true) @User LoginUser loginUser
    ) {
        MemberStyleResponse memberStyleResponse = memberStyleService.readMemberStyle(loginUser.getMemberId());
        return ResponseEntity.ok(memberStyleResponse);
    }

    @PutMapping
    public ResponseEntity<Void> updateMemberStyle(
            @Parameter(hidden = true) @User LoginUser loginUser,
            @RequestBody @Valid MemberStyleUpdateRequest memberStyleUpdateRequest) {
        memberStyleService.updateMemberStyle(loginUser.getMemberId(), memberStyleUpdateRequest);
        return ResponseEntity.noContent().build();
    }
}

