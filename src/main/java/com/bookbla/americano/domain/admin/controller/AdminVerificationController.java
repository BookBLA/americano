package com.bookbla.americano.domain.admin.controller;

import com.bookbla.americano.domain.admin.controller.dto.request.AdminMemberKakaoRoomStatusUpdateRequest;
import com.bookbla.americano.domain.admin.controller.dto.request.AdminMemberProfileImageStatusUpdateRequest;
import com.bookbla.americano.domain.admin.controller.dto.request.AdminMemberProfileStudentIdStatusUpdateRequest;
import com.bookbla.americano.domain.admin.service.AdminVerificationService;
import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin")
@RequiredArgsConstructor
@RestController
public class AdminVerificationController {

    private final AdminVerificationService adminVerificationService;

    @Operation(summary = "카카오톡 오픈 채팅방 상태 변경 API")
    @PatchMapping("/member-verifies/{memberVerifyId}/pending/kakao")
    public ResponseEntity<Void> updateKakaoRoomUrlPendingMemberStatus(
            @PathVariable Long memberVerifyId,
            @RequestBody @Valid AdminMemberKakaoRoomStatusUpdateRequest request
    ) {
        adminVerificationService.updateMemberKakaoRoomStatus(request.toDto(memberVerifyId));
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "프로필 이미지 상태 변경 API")
    @PatchMapping("/member-verifies/{memberVerifyId}/pending/profile-image")
    public ResponseEntity<Void> updateProfileImagePendingMemberStatus(
            @PathVariable Long memberVerifyId,
            @RequestBody @Valid AdminMemberProfileImageStatusUpdateRequest request
    ) {
        adminVerificationService.updateMemberImageStatus(request.toDto(memberVerifyId));
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "학생증 이미지 상태 변경 API")
    @PatchMapping("/member-verifies/{memberVerifyId}/pending/student-id/image")
    public ResponseEntity<Void> updateStudentIdPendingMemberStatus(
            @PathVariable Long memberVerifyId,
            @RequestBody @Valid AdminMemberProfileStudentIdStatusUpdateRequest request
    ) {
        adminVerificationService.updateMemberStudentIdStatus(request.toDto(memberVerifyId));
        return ResponseEntity.ok().build();
    }

}
