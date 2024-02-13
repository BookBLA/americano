package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.jwt.JwtProvider;
import com.bookbla.americano.base.jwt.LoginUser;
import com.bookbla.americano.domain.member.controller.dto.request.MemberCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberCreateResponse;
import com.bookbla.americano.domain.member.repository.MemberAuthRepository;
import com.bookbla.americano.domain.member.repository.MemberProfileRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import com.bookbla.americano.domain.member.service.MemberAuthService;
import com.bookbla.americano.domain.member.service.MemberProfileService;
import com.bookbla.americano.domain.member.service.MemberService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final MemberAuthService memberAuthService;
    private final MemberProfileService memberProfileService;

    @PostMapping
    public ResponseEntity<MemberCreateResponse> createMember(
        @RequestBody @Valid MemberCreateRequest memberCreateRequest,
        @LoginUser Long memberid) {

        Member member = memberRepository.getByIdOrThrow(memberid);

        MemberCreateResponse memberCreateResponse1 = memberProfileService.createProfile(
            memberCreateRequest.toDto(member));
        MemberCreateResponse memberCreateResponse2 = memberAuthService.createAuth(
            memberCreateRequest.toDto(member));

        // TODO: 학생증 인증 및 학교 이메일 인증 추가하기

        return ResponseEntity.ok(memberCreateResponse2);
    }
}
