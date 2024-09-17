package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookProfileRequestDto;
import com.bookbla.americano.domain.member.controller.dto.request.MemberInformationUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberStatusUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.*;
import com.bookbla.americano.domain.member.service.MemberBookmarkService;
import com.bookbla.americano.domain.member.service.MemberProfileService;
import com.bookbla.americano.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberBookmarkService memberBookmarkService;
    private final MemberProfileService memberProfileService;

    @Operation(summary = "사용자 정보 조회 API",
            description = "사용자의 oauth 정보, 타입, 상태 등을 조회<br>"
                    + "memberType : [APPLE, KAKAO, ADMIN]<br>"
                    + "memberStatus : [PROFILE, APPROVAL, STYLE, BOOK, COMPLETED, DELETED, BLOCKED, MATCHING_DISABLED, REPORTED]")
    @GetMapping
    public ResponseEntity<MemberResponse> readMember(
            @Parameter(hidden = true) @User LoginUser loginUser) {
        MemberResponse memberResponse = memberService.readMember(loginUser.getMemberId());
        return ResponseEntity.ok(memberResponse);
    }

    @Operation(summary = "멤버 상태 업데이트 API")
    @PostMapping("/status")
    public ResponseEntity<MemberStatusResponse> updateMemberStatus(
            @Parameter(hidden = true) @User LoginUser loginUser,
            @RequestBody @Valid MemberStatusUpdateRequest memberStatusUpdateRequest
    ) {
        MemberStatusResponse memberStatusResponse = memberService.updateStatus(loginUser.getMemberId(), memberStatusUpdateRequest.getMemberStatus());
        return ResponseEntity.ok(memberStatusResponse);
    }

    @Operation(summary = "사용자 계정 탈퇴 API",
            description = "탈퇴 시점(deletedAt)을 저장한 뒤에 30일 뒤에 회원 정보 삭제")
    @DeleteMapping
    public ResponseEntity<MemberDeleteResponse> deleteMember(
            @Parameter(hidden = true) @User LoginUser loginUser) {
        MemberDeleteResponse memberDeleteResponse = memberService.deleteMember(
                loginUser.getMemberId());
        return ResponseEntity.ok(memberDeleteResponse);
    }

    @Operation(summary = "사용자 상태 조회 API",
            description = "사용자의 상태를 조회<br>"
                    + "memberStatus : [PROFILE, APPROVAL, STYLE, BOOK, COMPLETED, DELETED, BLOCKED, MATCHING_DISABLED, REPORTED]")
    @GetMapping("/statuses")
    public ResponseEntity<MemberStatusResponse> readMemberStatus(
            @Parameter(hidden = true) @User LoginUser loginUser) {
        MemberStatusResponse memberStatusResponse = memberService.readMemberStatus(
                loginUser.getMemberId());
        return ResponseEntity.ok(memberStatusResponse);
    }

    @Operation(summary = "사용자 Bookmark 개수 조회", description = "책갈피 개수 조회 (기존:'/members/postcards')")
    @GetMapping("/bookmarks")
    public ResponseEntity<Integer> memberBookmarkCount(
            @Parameter(hidden = true) @User LoginUser loginUser) {
        return ResponseEntity.ok(
                memberBookmarkService.getMemberBookmarkCount(loginUser.getMemberId()));
    }

    @Operation(summary = "애드몹 시청", description = "애드몹 시청 종료 후 해당 API 호출")
    @PostMapping("/me/admob")
    public ResponseEntity<MemberBookmarkAdmobResponse> watchAdmob(
            @Parameter(hidden = true) @User LoginUser loginUser
    ) {
        var memberBookmarkAdmobResponse = memberBookmarkService.updateBookmarkByAdmob(loginUser.getMemberId());
        return ResponseEntity.ok(memberBookmarkAdmobResponse);
    }

    @Operation(summary = "애드몹 개수 조회", description = "잔여 애드몹 개수 조회")
    @GetMapping("/me/admob")
    public ResponseEntity<MemberBookmarkAdmobResponse> getAdmob(
            @Parameter(hidden = true) @User LoginUser loginUser
    ) {
        var memberBookmarkAdmobResponse = memberBookmarkService.getMemberAdmob(loginUser.getMemberId());
        return ResponseEntity.ok(memberBookmarkAdmobResponse);
    }

    @Operation(summary = "본인의 회원정보 수정")
    @PutMapping("/me/information")
    public ResponseEntity<Void> updateMemberInformation(
            @Parameter(hidden = true) @User LoginUser loginUser,
            @RequestBody @Valid MemberInformationUpdateRequest memberInformationUpdateRequest
    ) {
        memberService.updateMemberInformation(loginUser.getMemberId(), memberInformationUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "본인의 회원정보 조회")
    @GetMapping("/me/information")
    public ResponseEntity<MemberInformationReadResponse> readMemberInformation(@Parameter(hidden = true) @User LoginUser loginUser) {
        var response = memberService.readMemberInformation(loginUser.getMemberId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all-other-members")
    public ResponseEntity<Page<MemberBookProfileResponse>> getAllMembersProfile(
            @Parameter(hidden = true) @User LoginUser loginUser,
            @ModelAttribute MemberBookProfileRequestDto memberBookProfileRequestDto,
            Pageable pageable) {
        List<MemberBookProfileResponse> result = memberProfileService.getAllMembers(
                loginUser.getMemberId(), memberBookProfileRequestDto);
        if (pageable == null) {
            pageable = PageRequest.of(0, 0);
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), result.size());
        Page<MemberBookProfileResponse> memberBookProfileResponsePage;
        if (start >= result.size()) {
            memberBookProfileResponsePage = Page.empty();
        } else {
            memberBookProfileResponsePage = new PageImpl<>(
                    result.subList(start, end), pageable,
                    result.size());
        }
        return ResponseEntity.ok(memberBookProfileResponsePage);
    }
}
