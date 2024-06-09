package com.bookbla.americano.domain.auth.service.impl;

import com.bookbla.americano.base.jwt.JwtProvider;
import com.bookbla.americano.domain.auth.controller.dto.request.LoginRequest;
import com.bookbla.americano.domain.auth.controller.dto.response.LoginResponse;
import com.bookbla.americano.domain.auth.service.AuthService;
import com.bookbla.americano.domain.auth.service.OAuth2Provider;
import com.bookbla.americano.domain.auth.service.dto.OAuth2MemberResponse;
import com.bookbla.americano.domain.member.enums.MemberType;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.MemberStatusLogRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;
    private final MemberStatusLogRepository memberStatusLogRepository;
    private final OAuth2Providers oAuth2Providers;
    private final JwtProvider jwtProvider;
    private final TransactionTemplate transactionTemplate;

    @Override
    public LoginResponse login(LoginRequest loginRequest, String oAuthType) {
        MemberType memberType = MemberType.from(oAuthType);
        OAuth2Provider oAuth2Provider = oAuth2Providers.getProvider(memberType);
        OAuth2MemberResponse oAuth2MemberResponse = oAuth2Provider.getMemberResponse(
            loginRequest.getAuthCode());

        Member member = memberRepository.findByMemberTypeAndOauthEmail(memberType,
                oAuth2MemberResponse.getEmail())
            .orElseGet(() -> signUp(oAuth2MemberResponse));

        String accessToken = jwtProvider.createToken(member.getId().toString());
        return LoginResponse.of(accessToken, member);
    }

    private Member signUp(OAuth2MemberResponse oAuth2MemberResponse) {
        return transactionTemplate.execute(
            action -> memberRepository.save(oAuth2MemberResponse.toMember()));
    }
}
