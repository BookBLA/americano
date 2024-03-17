package com.bookbla.americano.domain.auth.service;

import com.bookbla.americano.base.jwt.JwtProvider;
import com.bookbla.americano.domain.auth.controller.dto.request.LoginRequestDto;
import com.bookbla.americano.domain.auth.controller.dto.response.LoginResponseDto;
import com.bookbla.americano.domain.auth.infra.kakao.dto.KakaoOAuth2MemberResponse;
import com.bookbla.americano.domain.auth.service.dto.OAuth2MemberResponse;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.enums.MemberType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final MemberRepository memberRepository;
    private final OAuth2Clients oAuth2Clients;
    private final JwtProvider jwtProvider;

    public LoginResponseDto login(LoginRequestDto loginRequestDto, String oAuthType) {
        MemberType memberType = MemberType.from(oAuthType);
        OAuth2Provider oAuth2Provider = oAuth2Clients.getProvider(memberType);

        KakaoOAuth2MemberResponse kakaoOAuth2MemberResponse = (KakaoOAuth2MemberResponse) oAuth2Provider.getMemberResponse(
            loginRequestDto.getAuthCode());

        Member member = memberRepository.findByMemberTypeAndOauthEmail(memberType,
                kakaoOAuth2MemberResponse.getEmail())
            .orElseGet(() -> memberRepository.save(kakaoOAuth2MemberResponse.toMember()));


        String accessToken = jwtProvider.createToken(member.getId().toString());
        return new LoginResponseDto(accessToken);
    }

}
