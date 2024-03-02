package com.bookbla.americano.domain.auth.service;

import com.bookbla.americano.base.jwt.JwtProvider;
import com.bookbla.americano.domain.auth.controller.dto.request.LoginRequestDto;
import com.bookbla.americano.domain.auth.controller.dto.response.LoginResponseDto;
import com.bookbla.americano.domain.auth.service.dto.OAuth2MemberResponse;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.MemberRepository;
import com.bookbla.americano.domain.member.enums.MemberType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final OAuth2Clients oAuth2Clients;
    private final JwtProvider jwtProvider;

    public LoginResponseDto login(LoginRequestDto loginRequestDto, String oAuthType) {
        MemberType memberType = MemberType.from(oAuthType);
        OAuth2Provider oAuth2Provider = oAuth2Clients.getProvider(memberType);

        OAuth2MemberResponse oAuth2MemberResponse = oAuth2Provider.getMemberResponse(loginRequestDto.getAuthCode());

        Member member = memberRepository.findByOauthEmailAndMemberType(oAuth2MemberResponse.getEmail(), memberType)
                .orElseGet(() -> memberRepository.save(oAuth2MemberResponse.toMember()));

        String accessToken = jwtProvider.createToken(member.getId().toString());
        return new LoginResponseDto(accessToken);
    }
  
}
