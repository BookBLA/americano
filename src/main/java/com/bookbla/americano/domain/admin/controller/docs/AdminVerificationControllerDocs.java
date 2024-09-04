package com.bookbla.americano.domain.admin.controller.docs;

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
