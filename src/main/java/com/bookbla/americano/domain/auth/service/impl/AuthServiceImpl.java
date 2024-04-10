package com.bookbla.americano.domain.auth.service.impl;

import com.bookbla.americano.base.jwt.JwtProvider;
import com.bookbla.americano.domain.auth.controller.dto.request.LoginRequestDto;
import com.bookbla.americano.domain.auth.controller.dto.response.LoginResponseDto;
import com.bookbla.americano.domain.auth.repository.MemberSignUpInformationRepository;
import com.bookbla.americano.domain.auth.repository.entity.MemberSignUpInformation;
import com.bookbla.americano.domain.auth.service.AuthService;
import com.bookbla.americano.domain.auth.service.OAuth2Provider;
import com.bookbla.americano.domain.auth.service.dto.OAuth2MemberResponse;
import com.bookbla.americano.domain.member.enums.MemberType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MemberSignUpInformationRepository memberSignUpInformationRepository;
    private final OAuth2Providers oAuth2Providers;
    private final JwtProvider jwtProvider;

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto, String oAuthType) {
        MemberType memberType = MemberType.from(oAuthType);
        OAuth2Provider oAuth2Provider = oAuth2Providers.getProvider(memberType);
        OAuth2MemberResponse oAuth2MemberResponse = oAuth2Provider.getMemberResponse(loginRequestDto.getAuthCode());

        MemberSignUpInformation memberSignUpInformation = memberSignUpInformationRepository.findByMemberTypeAndEmail(memberType, oAuth2MemberResponse.getEmail())
                .orElseGet(() -> memberSignUpInformationRepository.save(oAuth2MemberResponse.toMemberSignUpInformation()));

        String accessToken = jwtProvider.createToken(memberSignUpInformation.getId().toString());
        return LoginResponseDto.of(accessToken, memberSignUpInformation);
    }
  
}
