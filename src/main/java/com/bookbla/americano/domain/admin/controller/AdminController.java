package com.bookbla.americano.domain.admin.controller;

import com.bookbla.americano.domain.admin.controller.dto.request.AdminKakaoRoomUrlRequest;
import com.bookbla.americano.domain.admin.controller.dto.request.AdminProfileImageRequest;
import com.bookbla.americano.domain.admin.controller.dto.request.AdminStudentIdRequest;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberKakaoRoomResponses;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberProfileImageResponses;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberReadResponses;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberStudentIdResponses;
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

@RequiredArgsConstructor
@RequestMapping("/admin")
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
            @RequestBody @Valid AdminKakaoRoomUrlRequest request
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
            @RequestBody @Valid AdminProfileImageRequest request
    ) {
        adminMemberService.updateMemberImageStatus(request.toDto(memberId));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/members/pending/student-id")
    public ResponseEntity<AdminMemberStudentIdResponses> readStudentIdPendingMembers(Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize());
        AdminMemberStudentIdResponses adminMemberStudentIdResponses = adminMemberService.readStudentIdImagePendingMembers(pageRequest);
        return ResponseEntity.ok(adminMemberStudentIdResponses);
    }

    @PatchMapping("/members/{memberId}/pending/student-id")
    public ResponseEntity<Void> updateStudentIdPendingMembers(
            @PathVariable Long memberId,
            @RequestBody @Valid AdminStudentIdRequest request
    ) {
        adminMemberService.updateMemberStudentIdStatus(request.toDto(memberId));
        return ResponseEntity.ok().build();
    }

}
