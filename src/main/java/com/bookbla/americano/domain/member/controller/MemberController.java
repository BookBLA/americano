package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookProfileRequestDto;
import com.bookbla.americano.domain.member.controller.dto.request.MemberUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookProfileResponseDto;
import com.bookbla.americano.domain.member.controller.dto.response.MemberResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberStatusResponse;
import com.bookbla.americano.domain.member.service.MemberPostcardService;
import com.bookbla.americano.domain.member.service.MemberProfileService;
import com.bookbla.americano.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberPostcardService memberPostcardService;
    private final MemberProfileService memberProfileService;

    @GetMapping
    public ResponseEntity<MemberResponse> readMember(
        @Parameter(hidden = true) @User LoginUser loginUser) {
        MemberResponse memberResponse = memberService.readMember(loginUser.getMemberId());
        return ResponseEntity.ok(memberResponse);
    }

    @PutMapping
    public ResponseEntity<MemberResponse> updateMember(
        @Parameter(hidden = true) @User LoginUser loginUser,
        @RequestBody @Valid MemberUpdateRequest memberUpdateRequest) {
        MemberResponse memberResponse = memberService.updateMember(loginUser.getMemberId(),
            memberUpdateRequest);
        return ResponseEntity.ok(memberResponse);
    }

    @GetMapping("/statuses")
    public ResponseEntity<MemberStatusResponse> readMemberStatus(
        @Parameter(hidden = true) @User LoginUser loginUser) {
        MemberStatusResponse memberStatusResponse = memberService.readMemberStatus(
            loginUser.getMemberId());
        return ResponseEntity.ok(memberStatusResponse);
    }

    @GetMapping("/postcards")
    public ResponseEntity<Integer> memberPostcardCount(
        @Parameter(hidden = true) @User LoginUser loginUser) {
        Integer memberPostcardCount = memberPostcardService.getMemberPostcardCount(
            loginUser.getMemberId());
        return ResponseEntity.ok(memberPostcardCount);
    }

    @GetMapping("/same-book-members")
    public ResponseEntity<Page<MemberBookProfileResponseDto>> sameBookMembersPage(
        @Parameter(hidden = true) @User LoginUser loginUser,
        @ModelAttribute MemberBookProfileRequestDto memberBookProfileRequestDto,
        Pageable pageable) {
        List<MemberBookProfileResponseDto> memberBookProfileResponseList = memberProfileService.findSameBookMembers(
            loginUser.getMemberId(), memberBookProfileRequestDto);
        if (pageable == null) {
            pageable = PageRequest.of(0, 0);
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), memberBookProfileResponseList.size());
        Page<MemberBookProfileResponseDto> memberBookProfileResponsePage;
        if (start >= memberBookProfileResponseList.size()) {
            memberBookProfileResponsePage = Page.empty();
        } else {
            memberBookProfileResponsePage = new PageImpl<>(
                memberBookProfileResponseList.subList(start, end), pageable,
                memberBookProfileResponseList.size());
        }
        return ResponseEntity.ok(memberBookProfileResponsePage);
    }
}
