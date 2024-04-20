package com.bookbla.americano.domain.admin.controller;

import com.bookbla.americano.domain.admin.controller.dto.request.AdminMemberKakaoRoomStatusUpdateRequest;
import com.bookbla.americano.domain.admin.controller.dto.request.AdminMemberProfileImageStatusUpdateRequest;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberKakaoRoomResponses;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberProfileImageResponses;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberReadResponses;
import com.bookbla.americano.domain.admin.service.AdminMemberService;
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

    @GetMapping("/members/pending/kakao")
    public ResponseEntity<AdminMemberKakaoRoomResponses> readKakaoOpenRoomUrlPendingMembers(Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize());
        AdminMemberKakaoRoomResponses adminMemberKakaoRoomResponses = adminMemberService.readKakaoRoomPendingMembers(pageRequest);
        return ResponseEntity.ok(adminMemberKakaoRoomResponses);
    }

    @PatchMapping("/members/{memberId}/pending/kakao")
    public ResponseEntity<Void> updateKakaoRoomUrlPendingMemberStatus(
            @PathVariable Long memberId,
            @RequestBody @Valid AdminMemberKakaoRoomStatusUpdateRequest request
    ) {
        adminMemberService.updateMemberKakaoRoomStatus(request.toDto(memberId));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/members/pending/image")
    public ResponseEntity<AdminMemberProfileImageResponses> readProfileImagePendingMembers(Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize());
        AdminMemberProfileImageResponses adminMemberProfileImageResponses = adminMemberService.readProfileImagePendingMembers(pageRequest);
        return ResponseEntity.ok(adminMemberProfileImageResponses);
    }

    @PatchMapping("/members/{memberId}/pending/image")
    public ResponseEntity<Void> updateProfileImagePendingMemberStatus(
            @PathVariable Long memberId,
            @RequestBody @Valid AdminMemberProfileImageStatusUpdateRequest request
    ) {
        adminMemberService.updateMemberImageStatus(request.toDto(memberId));
        return ResponseEntity.ok().build();
    }
}
