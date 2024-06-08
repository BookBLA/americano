package com.bookbla.americano.domain.admin.controller;

import com.bookbla.americano.domain.admin.controller.dto.request.AdminMemberKakaoRoomStatusUpdateRequest;
import com.bookbla.americano.domain.admin.controller.dto.request.AdminMemberProfileImageStatusUpdateRequest;
import com.bookbla.americano.domain.admin.controller.dto.request.AdminMemberProfileStudentIdStatusUpdateRequest;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberKakaoRoomResponses;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberProfileImageResponses;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberProfileStatusResponse;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberReadResponses;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberStudentIdResponses;
import com.bookbla.americano.domain.admin.service.AdminMemberService;
import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin")
@RequiredArgsConstructor
@RestController
public class AdminMemberController {

    private final AdminMemberService adminMemberService;

    @Operation(summary = "회원 전체 조회")
    @GetMapping("/members")
    public ResponseEntity<AdminMemberReadResponses> readMembers(Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());
        AdminMemberReadResponses adminMemberReadResponses = adminMemberService.readMembers(pageRequest);
        return ResponseEntity.ok(adminMemberReadResponses);
    }

    @Operation(summary = "회원 프로필의 상태 목록 조회 API")
    @GetMapping("/member/profile/status")
    public ResponseEntity<AdminMemberProfileStatusResponse> getMemberStatuses() {
        AdminMemberProfileStatusResponse adminMemberProfileStatusResponse = adminMemberService.readProfileStatuses();
        return ResponseEntity.ok(adminMemberProfileStatusResponse);
    }

    @Operation(summary = "탈퇴 회원 목록 조회 API")
    @GetMapping("/members/deleted")
    public ResponseEntity<AdminMemberReadResponses> readDeletedMembers(Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());
        AdminMemberReadResponses adminMemberReadResponses = adminMemberService.readDeletedMembers(pageRequest);
        return ResponseEntity.ok(adminMemberReadResponses);
    }

    @Operation(summary = "카카오톡 오픈 승인 채팅방 대기 회원 조회 API")
    @GetMapping("/members/pending/kakao")
    public ResponseEntity<AdminMemberKakaoRoomResponses> readKakaoOpenRoomUrlPendingMembers(Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());
        AdminMemberKakaoRoomResponses adminMemberKakaoRoomResponses = adminMemberService.readKakaoRoomPendingMembers(pageRequest);
        return ResponseEntity.ok(adminMemberKakaoRoomResponses);
    }

    @Operation(summary = "카카오톡 오픈 채팅방 상태 변경 API")
    @PatchMapping("/member-verifies/{memberVerifyId}/pending/kakao")
    public ResponseEntity<Void> updateKakaoRoomUrlPendingMemberStatus(
            @PathVariable Long memberVerifyId,
            @RequestBody @Valid AdminMemberKakaoRoomStatusUpdateRequest request
    ) {
        adminMemberService.updateMemberKakaoRoomStatus(request.toDto(memberVerifyId));
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "프로필 이미지 승인 대기 회원 조회 API")
    @GetMapping("/members/pending/profile-image")
    public ResponseEntity<AdminMemberProfileImageResponses> readProfileImagePendingMembers(Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());
        AdminMemberProfileImageResponses adminMemberProfileImageResponses = adminMemberService.readProfileImagePendingMembers(pageRequest);
        return ResponseEntity.ok(adminMemberProfileImageResponses);
    }

    @Operation(summary = "프로필 이미지 상태 변경 API")
    @PatchMapping("/member-verifies/{memberVerifyId}/pending/profile-image")
    public ResponseEntity<Void> updateProfileImagePendingMemberStatus(
            @PathVariable Long memberVerifyId,
            @RequestBody @Valid AdminMemberProfileImageStatusUpdateRequest request
    ) {
        adminMemberService.updateMemberImageStatus(request.toDto(memberVerifyId));
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "학생증 승인 대기 회원 조회 API")
    @GetMapping("/members/pending/student-id/image")
    public ResponseEntity<AdminMemberStudentIdResponses> readStudentIdPendingMembers(Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());
        AdminMemberStudentIdResponses adminMemberStudentIdResponses = adminMemberService.readStudentIdImagePendingMembers(pageRequest);
        return ResponseEntity.ok(adminMemberStudentIdResponses);
    }

    @Operation(summary = "학생증 이미지 상태 변경 API")
    @PatchMapping("/member-verifies/{memberVerifyId}/pending/student-id/image")
    public ResponseEntity<Void> updateStudentIdPendingMemberStatus(
            @PathVariable Long memberVerifyId,
            @RequestBody @Valid AdminMemberProfileStudentIdStatusUpdateRequest request
    ) {
        adminMemberService.updateMemberStudentIdStatus(request.toDto(memberVerifyId));
        return ResponseEntity.ok().build();
    }
}
