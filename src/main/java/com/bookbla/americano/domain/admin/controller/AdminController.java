package com.bookbla.americano.domain.admin.controller;

import com.bookbla.americano.domain.admin.controller.dto.request.AdminMemberKakaoRoomStatusUpdateRequest;
import com.bookbla.americano.domain.admin.controller.dto.request.AdminMemberProfileImageStatusUpdateRequest;
import com.bookbla.americano.domain.admin.controller.dto.request.AdminMemberStatusUpdateRequest;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberKakaoRoomResponses;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberProfileImageResponses;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberReadResponses;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberStudentIdResponses;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminPendingMemberResponses;
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
public class AdminController {

    private final AdminMemberService adminMemberService;

    @GetMapping("/members")
    public ResponseEntity<AdminMemberReadResponses> readMembers(Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize());
        AdminMemberReadResponses adminMemberReadResponses = adminMemberService.readMembers(pageRequest);
        return ResponseEntity.ok(adminMemberReadResponses);
    }

    @Operation(summary = "회원 승인 대기중인 목록 조회 API")
    @GetMapping("/members/approval")
    public ResponseEntity<AdminPendingMemberResponses> readPendingMembers(Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize());
        AdminPendingMemberResponses adminPendingMemberResponses = adminMemberService.readPendingMembers(pageRequest);
        return ResponseEntity.ok(adminPendingMemberResponses);
    }


    @GetMapping("/members/pending/kakao")
    public ResponseEntity<AdminMemberKakaoRoomResponses> readKakaoOpenRoomUrlPendingMembers(Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize());
        AdminMemberKakaoRoomResponses adminMemberKakaoRoomResponses = adminMemberService.readKakaoRoomPendingMembers(pageRequest);
        return ResponseEntity.ok(adminMemberKakaoRoomResponses);
    }

    @PatchMapping("/member-verifies/{memberVerifyId}/pending/kakao")
    public ResponseEntity<Void> updateKakaoRoomUrlPendingMemberStatus(
            @PathVariable Long memberVerifyId,
            @RequestBody @Valid AdminMemberKakaoRoomStatusUpdateRequest request
    ) {
        adminMemberService.updateMemberKakaoRoomStatus(request.toDto(memberVerifyId));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/members/pending/image")
    public ResponseEntity<AdminMemberProfileImageResponses> readProfileImagePendingMembers(Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize());
        AdminMemberProfileImageResponses adminMemberProfileImageResponses = adminMemberService.readProfileImagePendingMembers(pageRequest);
        return ResponseEntity.ok(adminMemberProfileImageResponses);
    }

    @PatchMapping("/member-verifies/{memberVerifyId}/pending/image")
    public ResponseEntity<Void> updateProfileImagePendingMemberStatus(
            @PathVariable Long memberVerifyId,
            @RequestBody @Valid AdminMemberProfileImageStatusUpdateRequest request
    ) {
        adminMemberService.updateMemberImageStatus(request.toDto(memberVerifyId));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/members/pending/student-id/image")
    public ResponseEntity<AdminMemberStudentIdResponses> readStudentIdPendingMembers(Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize());
        AdminMemberStudentIdResponses adminMemberStudentIdResponses = adminMemberService.readStudentIdImagePendingMembers(pageRequest);
        return ResponseEntity.ok(adminMemberStudentIdResponses);
    }

    @PatchMapping("/member-verifies/{memberVerifyId}/pending/studentId/image")
    public ResponseEntity<Void> updateStudentIdPendingMemberStatus(
            @PathVariable Long memberVerifyId,
            @RequestBody @Valid AdminMemberProfileImageStatusUpdateRequest request
    ) {
        adminMemberService.updateMemberStudentIdStatus(request.toDto(memberVerifyId));
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원 상태 업데이트 API")
    @PatchMapping("/members/{memberId}/profile/status")
    public ResponseEntity<Void> updateMemberStatus(
            @PathVariable Long memberId,
            @RequestBody @Valid AdminMemberStatusUpdateRequest request
    ) {
        adminMemberService.updatePendingMemberStatus(request.toDto(memberId));
        return ResponseEntity.ok().build();
    }
}
