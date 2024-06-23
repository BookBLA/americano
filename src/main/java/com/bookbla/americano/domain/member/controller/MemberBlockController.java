package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBlockCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBlockCreateResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBlockDeleteResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBlockReadResponse;
import com.bookbla.americano.domain.member.service.MemberBlockService;
import io.swagger.v3.oas.annotations.Parameter;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member-blocks")
public class MemberBlockController {

    private final MemberBlockService memberBlockService;

    @PostMapping
    public ResponseEntity<MemberBlockCreateResponse> addMemberBlock(
        @Parameter(hidden = true) @User LoginUser loginUser,
        @RequestBody @Valid MemberBlockCreateRequest memberBlockCreateRequest) {
        MemberBlockCreateResponse memberBlockCreateResponse = memberBlockService.addMemberBlock(
            loginUser.getMemberId(), memberBlockCreateRequest);

        return ResponseEntity.ok(memberBlockCreateResponse);
    }

    @GetMapping
    public ResponseEntity<MemberBlockReadResponse> readMemberBlock(
        @Parameter(hidden = true) @User LoginUser loginUser) {
        MemberBlockReadResponse memberBlockReadResponse = memberBlockService.readMemberBlock(
            loginUser.getMemberId());

        return ResponseEntity.ok(memberBlockReadResponse);
    }

    @DeleteMapping("/{memberBlockId}")
    public ResponseEntity<MemberBlockDeleteResponse> deleteMemberBlock(
        @Parameter(hidden = true) @User LoginUser loginUser,
        @PathVariable Long memberBlockId) {
        MemberBlockDeleteResponse memberBlockDeleteResponse = memberBlockService.deleteMemberBlock(
            loginUser.getMemberId(), memberBlockId);

        return ResponseEntity.ok(memberBlockDeleteResponse);
    }
}
