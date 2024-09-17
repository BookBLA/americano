package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.member.controller.dto.request.MemberProfileCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberProfileStudentIdImageUrlUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberProfileUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberNameVerifyResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberProfileResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberProfileStatusResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberProfileStudentIdReadResponse;
import com.bookbla.americano.domain.member.service.MemberProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member-profiles")
@RequiredArgsConstructor
public class MemberProfileController {

    private final MemberProfileService memberProfileService;

    @Operation(summary = "사용자 프로필 정보 생성 API",
        description = "해당 사용자의 프로필 정보들을 생성<br>"
            + "gender(성별) -> [MALE, FEMALE] | schoolEmail(학교 이메일) -> abc123@gachon.ac.kr")
    @PostMapping
    public ResponseEntity<MemberProfileResponse> createMemberProfile(
        @Parameter(hidden = true) @User LoginUser loginUser,
        @RequestBody @Valid MemberProfileCreateRequest memberProfileCreateRequest) {
        MemberProfileResponse memberProfileResponse = memberProfileService.createMemberProfile(
            loginUser.getMemberId(), memberProfileCreateRequest.toMemberProfileDto());

        return ResponseEntity.ok(memberProfileResponse);
    }

    @Operation(summary = "사용자 프로필 정보 조회 API",
        description = "해당 사용자의 프로필 정보들을 확인<br>"
            + "gender(성별) -> [MALE, FEMALE] | schoolEmail(학교 이메일) -> abc123@gachon.ac.kr")
    @GetMapping
    public ResponseEntity<MemberProfileResponse> readMemberProfile(
        @Parameter(hidden = true) @User LoginUser loginUser) {
        MemberProfileResponse memberProfileResponse = memberProfileService.readMemberProfile(
            loginUser.getMemberId());
        return ResponseEntity.ok(memberProfileResponse);
    }

    @Operation(summary = "사용자 프로필 정보 수정 API",
        description = "해당 사용자의 프로필 정보들을 수정<br>"
            + "gender(성별) -> [MALE, FEMALE] | schoolEmail(학교 이메일) -> abc123@gachon.ac.kr")
    @PutMapping
    public ResponseEntity<MemberProfileResponse> updateMemberProfile(
        @Parameter(hidden = true) @User LoginUser loginUser,
        @RequestBody @Valid MemberProfileUpdateRequest memberProfileUpdateRequest
    ) {
        MemberProfileResponse memberProfileResponse = memberProfileService.updateMemberProfile(
            loginUser.getMemberId(), memberProfileUpdateRequest);
        return ResponseEntity.ok(memberProfileResponse);
    }
    @Operation(summary = "사용자 프로필 정보 中 상태 조회 API",
        description = "해당 사용자가 회원 승인할 때 필요한 상태를 조회<br>"
            + "profileImageUrlStatus, studentIdImageStatus : [PENDING, DENIAL, DONE]<br>"
            + "openKakaoRoomStatus : [PENDING, INACCESSIBLE, NOT_DEFAULT, DONE]")
    @GetMapping("/statuses")
    public ResponseEntity<MemberProfileStatusResponse> readMemberProfileStatus(
        @Parameter(hidden = true) @User LoginUser loginUser) {
        MemberProfileStatusResponse memberProfileStatusResponse = memberProfileService.readMemberProfileStatus(
            loginUser.getMemberId());

        return ResponseEntity.ok(memberProfileStatusResponse);
    }

    @Operation(summary = "사용자 학생증 이미지 저장 API")
    @PostMapping("/student-id/image")
    public ResponseEntity<Void> updateMemberStudentIdImage(
        @Parameter(hidden = true) @User LoginUser loginUser,
        @RequestBody @Valid MemberProfileStudentIdImageUrlUpdateRequest request
    ) {
        memberProfileService.updateMemberProfileStudentId(loginUser.getMemberId(), request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "사용자 학생증 검증 상태 조회 API")
    @GetMapping("/student-id/status")
    public ResponseEntity<MemberProfileStudentIdReadResponse> getMemberStudentIdStatus(
        @Parameter(hidden = true) @User LoginUser loginUser
    ) {
        MemberProfileStudentIdReadResponse response = memberProfileService.readMemberProfileStudentIdStatus(
            loginUser.getMemberId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "사용자 닉네임 검증 API")
    @PostMapping("/verify/name")
    public ResponseEntity<MemberNameVerifyResponse> verifyMemberName(
        @RequestBody String name
    ) {
        return ResponseEntity.ok(memberProfileService.verifyMemberName(name));
    }
}
