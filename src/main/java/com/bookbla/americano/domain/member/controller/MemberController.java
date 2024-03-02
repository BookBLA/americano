package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.jwt.LoginUser;
import com.bookbla.americano.domain.member.controller.dto.request.MemberUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberResponse;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookProfileRequestDto;
import com.bookbla.americano.domain.member.controller.dto.request.MemberPostcardCountRequestDto;
import com.bookbla.americano.domain.member.controller.dto.response.MailSendResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MailVerifyResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookProfileResponseDto;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.service.MemberService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<MemberResponse> readMember(@LoginUser Long memberId) {

        MemberResponse memberResponse = memberService.readMember(memberId);
        return ResponseEntity.ok(memberResponse);
    }

    @PutMapping
    public ResponseEntity<MemberResponse> updateMember(
        @RequestBody @Valid MemberUpdateRequest memberUpdateRequest,
        @LoginUser Long memberId) {

        MemberResponse memberResponse = memberService.updateMember(memberId, memberUpdateRequest);
        return ResponseEntity.ok(memberResponse);
    }

    @GetMapping("/memberPostcardCount")
    public ResponseEntity<Integer> memberPostcardCount(@ModelAttribute MemberPostcardCountRequestDto memberPostcardCountRequestDto) {
        Integer memberPostcardCount = memberService.getMemberPostcardCount(memberPostcardCountRequestDto);
        return ResponseEntity.ok(memberPostcardCount);
    }


    @GetMapping("/sameBookMembers")
    public ResponseEntity<Page<MemberBookProfileResponseDto>> sameBookMembersPage(@ModelAttribute MemberBookProfileRequestDto memberBookProfileRequestDto, Pageable pageable) {
        List<MemberBookProfileResponseDto> memberBookProfileResponseList = memberService.findSameBookMembers(memberBookProfileRequestDto);
        if(pageable == null)
            pageable = PageRequest.of(0,0);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), memberBookProfileResponseList.size());
        Page<MemberBookProfileResponseDto> memberBookProfileResponsePage;
        if(start >= memberBookProfileResponseList.size())
            memberBookProfileResponsePage = Page.empty();
        else
            memberBookProfileResponsePage = new PageImpl<>(memberBookProfileResponseList.subList(start, end), pageable, memberBookProfileResponseList.size());

        return ResponseEntity.ok(memberBookProfileResponsePage);
    }
}
