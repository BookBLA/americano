package com.bookbla.americano.domain.member.controller;


import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.member.controller.dto.request.MemberReportCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberReportCreateResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberReportDeleteResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberReportReadResponse;
import com.bookbla.americano.domain.member.service.MemberReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member-reports")
public class MemberReportController {

    private final MemberReportService memberReportService;

    @Operation(summary = "상대방 신고하기")
    @PostMapping
    public ResponseEntity<MemberReportCreateResponse> addMemberReport(
        @Parameter(hidden = true) @User LoginUser loginUser,
        @RequestBody @Valid MemberReportCreateRequest memberReportCreateRequest) {
        MemberReportCreateResponse memberReportCreateResponse = memberReportService.addMemberReport(
            loginUser.getMemberId(), memberReportCreateRequest);

        return ResponseEntity.ok(memberReportCreateResponse);
    }

    @Operation(summary = "신고 조회")
    @GetMapping
    public ResponseEntity<MemberReportReadResponse> readMemberReport(
        @Parameter(hidden = true) @User LoginUser loginUser) {
        MemberReportReadResponse memberReportReadResponse = memberReportService.readMemberReport(
            loginUser.getMemberId());

        return ResponseEntity.ok(memberReportReadResponse);
    }

    @DeleteMapping("/{memberReportId}")
    public ResponseEntity<MemberReportDeleteResponse> deleteMemberReport(
        @Parameter(hidden = true) @User LoginUser loginUser,
        @PathVariable Long memberReportId) {
        MemberReportDeleteResponse memberReportDeleteResponse = memberReportService.deleteMemberReport(
            loginUser.getMemberId(), memberReportId);

        return ResponseEntity.ok(memberReportDeleteResponse);
    }

}
