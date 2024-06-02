package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.member.controller.dto.request.MemberContactCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberContactCreateResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberContactReadResponse;
import com.bookbla.americano.domain.member.service.MemberContactService;
import io.swagger.v3.oas.annotations.Parameter;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members/contacts")
public class MemberContactController {

    private final MemberContactService memberContactService;

    @PostMapping
    public ResponseEntity<MemberContactCreateResponse> createMemberContacts(
        @Parameter(hidden = true) @User LoginUser loginUser,
        @RequestBody @Valid MemberContactCreateRequest memberContactCreateRequest) {

        MemberContactCreateResponse memberContactCreateResponse = memberContactService.createMemberContacts(
            loginUser.getMemberId(), memberContactCreateRequest);

        return ResponseEntity.ok(memberContactCreateResponse);
    }

    @GetMapping
    public ResponseEntity<MemberContactReadResponse> readMemberContacts(
        @Parameter(hidden = true) @User LoginUser loginUser) {

        MemberContactReadResponse memberContactReadResponse = memberContactService.readMemberContacts(
            loginUser.getMemberId());

        return ResponseEntity.ok(memberContactReadResponse);
    }
}
