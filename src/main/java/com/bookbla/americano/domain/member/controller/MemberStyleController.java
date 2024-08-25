package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.member.controller.docs.MemberStyleControllerDocs;
import com.bookbla.americano.domain.member.controller.dto.request.MemberProfileImageTypeUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberStyleCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberStyleUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberStyleResponse;
import com.bookbla.americano.domain.member.service.MemberStyleService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members/styles")
public class MemberStyleController implements MemberStyleControllerDocs {

    private final MemberStyleService memberStyleService;

    @PostMapping
    public ResponseEntity<MemberStyleResponse> createMemberStyle(
            @User LoginUser loginUser,
            @RequestBody @Valid MemberStyleCreateRequest memberStyleCreateRequest
    ) {
        MemberStyleResponse memberStyleResponse = memberStyleService.createMemberStyle(loginUser.getMemberId(), memberStyleCreateRequest);
        return ResponseEntity.ok(memberStyleResponse);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberStyleResponse> readMemberStyle(@PathVariable Long memberId) {
        MemberStyleResponse memberStyleResponse = memberStyleService.readMemberStyle(memberId);
        return ResponseEntity.ok(memberStyleResponse);
    }

    @PutMapping
    public ResponseEntity<Void> updateMemberStyle(
            @User LoginUser loginUser,
            @RequestBody @Valid MemberStyleUpdateRequest memberStyleUpdateRequest
    ) {
        memberStyleService.updateMemberStyle(loginUser.getMemberId(), memberStyleUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "수정 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 프로필 이미지 id")
    })
    @PatchMapping("/profile-image-type")
    public ResponseEntity<Void> updateMemberStyleProfileImageType(
            @User LoginUser loginUser,
            @RequestBody @Valid MemberProfileImageTypeUpdateRequest request
    ) {
        memberStyleService.updateMemberProfileImageType(loginUser.getMemberId(), request.getProfileImageTypeId());
        return ResponseEntity.noContent().build();
    }
}

