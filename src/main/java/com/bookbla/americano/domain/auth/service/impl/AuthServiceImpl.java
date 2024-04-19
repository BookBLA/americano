package com.bookbla.americano.domain.auth.service.impl;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.base.jwt.JwtProvider;
import com.bookbla.americano.domain.auth.controller.dto.request.LoginRequestDto;
import com.bookbla.americano.domain.auth.controller.dto.request.RejoinRequest;
import com.bookbla.americano.domain.auth.controller.dto.response.LoginResponseDto;
import com.bookbla.americano.domain.auth.controller.dto.response.RejoinResponse;
import com.bookbla.americano.domain.auth.exception.LoginExceptionType;
import com.bookbla.americano.domain.auth.service.AuthService;
import com.bookbla.americano.domain.auth.service.OAuth2Provider;
import com.bookbla.americano.domain.auth.service.dto.OAuth2MemberResponse;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.enums.MemberType;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;
    private final OAuth2Providers oAuth2Providers;
    private final JwtProvider jwtProvider;

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto, String oAuthType) {
        MemberType memberType = MemberType.from(oAuthType);
        OAuth2Provider oAuth2Provider = oAuth2Providers.getProvider(memberType);
        OAuth2MemberResponse oAuth2MemberResponse = oAuth2Provider.getMemberResponse(loginRequestDto.getAuthCode());

        Member member = memberRepository.findByMemberTypeAndOauthEmail(memberType, oAuth2MemberResponse.getEmail())
            .orElseGet(() -> memberRepository.save(oAuth2MemberResponse.toMember()));

        // 회원이 탈퇴 상태라면
        if (member.getMemberStatus() == MemberStatus.DELETED) {
            throw new BaseException(LoginExceptionType.CANNOT_LOGIN_MEMBER_DELETED);
        }

        String accessToken = jwtProvider.createToken(member.getId().toString());
        return LoginResponseDto.of(accessToken, member);
    }

    @Override
    public RejoinResponse rejoin(RejoinRequest rejoinRequest, String oAuthType) {
        MemberType memberType = MemberType.from(oAuthType);
        OAuth2Provider oAuth2Provider = oAuth2Providers.getProvider(memberType);
        OAuth2MemberResponse oAuth2MemberResponse = oAuth2Provider.getMemberResponse(rejoinRequest.getAuthCode());

        Member member = memberRepository.findByMemberTypeAndOauthEmail(memberType, oAuth2MemberResponse.getEmail())
            .orElseGet(() -> memberRepository.save(oAuth2MemberResponse.toMember()));

        LocalDateTime deleteAtThirtyDaysAfter = member.getDeleteAt().minusDays(30);

        if (member.getMemberStatus() == MemberStatus.DELETED) {
            // 회원 탈퇴 한 뒤에 30일이 지났다면
            if (LocalDateTime.now().isAfter(deleteAtThirtyDaysAfter)) {
                member.updateMemberStatus(MemberStatus.COMPLETED);
            }
            // 회원 탈퇴 한 뒤에 30일이 지나지 않았다면
            else {
               throw new BaseException(LoginExceptionType.NOT_THIRTY_DAYS_AFTER);
            }
        } else {
            throw new BaseException(LoginExceptionType.NOT_MEMBER_DELETED);
        }

        String accessToken = jwtProvider.createToken(member.getId().toString());
        return RejoinResponse.from(accessToken, member);
    }
}
