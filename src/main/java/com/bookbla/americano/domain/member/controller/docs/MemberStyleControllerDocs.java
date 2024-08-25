package com.bookbla.americano.domain.member.controller.docs;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.member.controller.dto.request.MemberProfileImageTypeUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberStyleCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberStyleUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberStyleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "회원 스타일", description = "회원 스타일 관련 API 모음")
public interface MemberStyleControllerDocs {

    @Operation(summary = "회원 스타일 생성")
    @ApiResponse(responseCode = "200")
    @PostMapping
    ResponseEntity<MemberStyleResponse> createMemberStyle(
            @Parameter(hidden = true) @User LoginUser loginUser,
            @RequestBody @Valid MemberStyleCreateRequest memberStyleCreateRequest
    );

    @Operation(summary = "회원 스타일 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping
    ResponseEntity<MemberStyleResponse> readMemberStyle(@PathVariable Long memberId);

    @Operation(summary = "회원 스타일 수정")
    @ApiResponse(responseCode = "204")
    @PutMapping
    ResponseEntity<Void> updateMemberStyle(
            @Parameter(hidden = true) @User LoginUser loginUser,
            @RequestBody @Valid MemberStyleUpdateRequest memberStyleUpdateRequest
    );

    @Operation(summary = "회원 스타일 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "수정 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 프로필 이미지 id")
    })
    @PatchMapping
    ResponseEntity<Void> updateMemberStyleProfileImageType(
            @Parameter(hidden = true) @User LoginUser loginUser,
            @RequestBody @Valid MemberProfileImageTypeUpdateRequest request
    );
}
