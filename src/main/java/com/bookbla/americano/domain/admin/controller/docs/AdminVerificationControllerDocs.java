package com.bookbla.americano.domain.admin.controller.docs;

import com.bookbla.americano.domain.admin.controller.dto.request.AdminMemberKakaoRoomStatusUpdateRequest;
import com.bookbla.americano.domain.admin.controller.dto.request.AdminMemberProfileImageStatusUpdateRequest;
import com.bookbla.americano.domain.admin.controller.dto.request.AdminMemberProfileStudentIdStatusUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "관리자 회원 관리", description = "회원 인증상태 관리용 API 모음입니다.")
public interface AdminVerificationControllerDocs {

    @Operation(summary = "카카오톡 오픈 채팅방 상태 변경 API")
    @ApiResponse(
            responseCode = "200",
            description = "카카오톡 오픈 채팅방 상태 변경 성공"
    )
    @PatchMapping
    ResponseEntity<Void> updateKakaoRoomUrlPendingMemberStatus(
            @PathVariable Long memberVerifyId,
            @RequestBody @Valid AdminMemberKakaoRoomStatusUpdateRequest request
    );

    @Operation(summary = "프로필 이미지 상태 변경 API")
    @ApiResponse(
            responseCode = "200",
            description = "프로필 이미지 상태 변경 성공"
    )
    @PatchMapping
    ResponseEntity<Void> updateProfileImagePendingMemberStatus(
            @PathVariable Long memberVerifyId,
            @RequestBody @Valid AdminMemberProfileImageStatusUpdateRequest request
    );

    @Operation(summary = "학생증 이미지 상태 변경 API")
    @ApiResponse(
            responseCode = "200",
            description = "학생증 이미지 상태 변경 성공"
    )
    @PatchMapping
    ResponseEntity<Void> updateStudentIdPendingMemberStatus(
            @PathVariable Long memberVerifyId,
            @RequestBody @Valid AdminMemberProfileStudentIdStatusUpdateRequest request
    );
}
