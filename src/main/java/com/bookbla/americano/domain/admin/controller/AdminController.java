package com.bookbla.americano.domain.admin.controller;

import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberAuthResponses;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberReadResponses;
import com.bookbla.americano.domain.admin.service.AdminMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/members/approve")
    public ResponseEntity<AdminMemberAuthResponses> readApprovalStatusMembers(Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize());
        AdminMemberAuthResponses adminMemberAuthResponses = adminMemberService.readApprovalStatusMembers(pageRequest);
        return ResponseEntity.ok(adminMemberAuthResponses);
    }
}
