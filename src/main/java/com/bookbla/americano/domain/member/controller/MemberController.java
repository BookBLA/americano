package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.jwt.LoginUser;
import com.bookbla.americano.domain.member.controller.dto.request.MemberUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberResponse;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookProfileRequestDto;
import com.bookbla.americano.domain.member.controller.dto.request.MemberCoinCountRequestDto;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookProfileResponseDto;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.service.MemberService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/memberCoinCount")
    public ResponseEntity<Integer> memberCoinCount(@ModelAttribute MemberCoinCountRequestDto memberCoinCountRequestDto){
        Integer memberCoinCount = memberService.getMemberCoinCount(memberCoinCountRequestDto);
        return ResponseEntity.ok(memberCoinCount);
    }

    @GetMapping("/sameBookMembers")
    public ResponseEntity<List<MemberBookProfileResponseDto>> sameBookMembers(@ModelAttribute MemberBookProfileRequestDto memberBookProfileRequestDto) {
        List<MemberBookProfileResponseDto> memberBookProfileResponseList = memberService.findSameBookMembers(memberBookProfileRequestDto);
        return ResponseEntity.ok(memberBookProfileResponseList);
    }
}
