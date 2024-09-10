package com.bookbla.americano.domain.admin.controller;

import com.bookbla.americano.domain.admin.controller.docs.AdminMemberControllerDocs;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberProfileStatusResponse;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberReadResponses;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberStudentIdResponses;
import com.bookbla.americano.domain.admin.service.AdminMemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin")
@RequiredArgsConstructor
@RestController
public class AdminMemberController implements AdminMemberControllerDocs {

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

    @Operation(summary = "학생증 승인 대기 회원 조회 API")
    @GetMapping("/members/pending/student-id/image")
    public ResponseEntity<AdminMemberStudentIdResponses> readStudentIdPendingMembers(Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());
        AdminMemberStudentIdResponses adminMemberStudentIdResponses = adminMemberService.readStudentIdImagePendingMembers(pageRequest);
        return ResponseEntity.ok(adminMemberStudentIdResponses);
    }

}
