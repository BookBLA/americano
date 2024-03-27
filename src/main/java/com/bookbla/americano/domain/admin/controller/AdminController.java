package com.bookbla.americano.domain.admin.controller;

import java.awt.print.Pageable;

import com.bookbla.americano.domain.admin.controller.dto.request.AdminLoginRequest;
import com.bookbla.americano.domain.admin.controller.dto.request.AdminMemberProfileStatusUpdateRequest;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminLoginResponse;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberProfileImageResponses;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberProfileKakaoRoomResponses;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberProfileStudentIdImageResponses;
import com.bookbla.americano.domain.admin.service.AdminAuthService;
import com.bookbla.americano.domain.admin.service.AdminService;
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
@RequestMapping("/admin")
@RestController
public class AdminController {

    private final AdminAuthService adminAuthService;
    private final AdminService adminService;

    @PostMapping("/auth/login")
    public ResponseEntity<AdminLoginResponse> login(@RequestBody @Valid AdminLoginRequest adminLoginRequest) {
        AdminLoginResponse adminLoginResponse = adminAuthService.login(adminLoginRequest);
        return ResponseEntity.ok(adminLoginResponse);
    }

    @GetMapping("/member-profiles/kakao-room/pending")
    public ResponseEntity<AdminMemberProfileKakaoRoomResponses> readOpenKakaoRoomPendingMemberProfiles(Pageable pageable) {
        AdminMemberProfileKakaoRoomResponses adminMemberProfileKakaoRoomResponses = adminService.readOpenKakaoRoomPendingMemberProfiles(pageable);
        return ResponseEntity.ok(adminMemberProfileKakaoRoomResponses);
    }

    @PatchMapping("/member-profiles/{memberProfileId}/kakao-room-status")
    public ResponseEntity<Void> updateMemberProfileKakaoRoomStatus(
            @PathVariable Long memberProfileId,
            @RequestBody @Valid AdminMemberProfileStatusUpdateRequest request
    ) {
        adminService.updateMemberProfileKakaoRoomStatus(request.toDto(memberProfileId));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/member-profiles/profile-image/pending")
    public ResponseEntity<AdminMemberProfileImageResponses> readProfileImagePendingMemberProfiles(Pageable pageable) {
        AdminMemberProfileImageResponses adminMemberProfileImageResponses = adminService.readProfileImagePendingMemberProfiles(pageable);
        return ResponseEntity.ok(adminMemberProfileImageResponses);
    }

    @PatchMapping("/member-profiles/{memberProfileId}/profile-image-status")
    public ResponseEntity<Void> updateMemberProfileImageStatus(
            @PathVariable Long memberProfileId,
            @RequestBody @Valid AdminMemberProfileStatusUpdateRequest request
    ) {
        adminService.updateMemberProfileImageStatus(request.toDto(memberProfileId));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/member-profiles/student-id/pending")
    public ResponseEntity<AdminMemberProfileStudentIdImageResponses> readStudentIdImagePendingMemberProfiles(Pageable pageable) {
        AdminMemberProfileStudentIdImageResponses adminMemberProfileStudentIdImageResponses = adminService.readStudentIdImagePendingMemberProfiles(pageable);
        return ResponseEntity.ok(adminMemberProfileStudentIdImageResponses);
    }

    @PatchMapping("/member-profiles/{memberProfileId}/student-id-status")
    public ResponseEntity<Void> updateMemberProfileStudentIdStatus(
            @PathVariable Long memberProfileId,
            @RequestBody @Valid AdminMemberProfileStatusUpdateRequest request
    ) {
        adminService.updateMemberProfileStudentIdImageStatus(request.toDto(memberProfileId));
        return ResponseEntity.noContent().build();
    }
}
