package com.bookbla.americano.domain.admin.controller;

import com.bookbla.americano.domain.admin.controller.docs.AdminVerificationControllerDocs;
import com.bookbla.americano.domain.admin.controller.dto.request.AdminMemberProfileStudentIdStatusUpdateRequest;
import com.bookbla.americano.domain.admin.service.AdminVerificationService;
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
public class AdminVerificationController implements AdminVerificationControllerDocs {

    private final AdminVerificationService adminVerificationService;

    @PatchMapping("/member-verifies/{memberVerifyId}/pending/student-id/image")
    public ResponseEntity<Void> updateStudentIdPendingMemberStatus(
            @PathVariable Long memberVerifyId,
            @RequestBody @Valid AdminMemberProfileStudentIdStatusUpdateRequest request
    ) {
        adminVerificationService.updateMemberStudentIdStatus(request.toDto(memberVerifyId));
        return ResponseEntity.ok().build();
    }

}
