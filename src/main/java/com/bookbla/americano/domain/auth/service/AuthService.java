package com.bookbla.americano.domain.auth.service;

import com.bookbla.americano.base.jwt.JwtProvider;
import com.bookbla.americano.domain.auth.controller.dto.request.LoginRequest;
import com.bookbla.americano.domain.auth.controller.dto.response.LoginResponse;
import com.bookbla.americano.domain.auth.service.dto.OAuth2MemberResponse;
import com.bookbla.americano.domain.auth.service.dto.event.AdminNotificationEventWithoutTransaction;
import com.bookbla.americano.domain.member.enums.MemberType;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.service.dto.event.AdminNotificationEventWithAfterCommit;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final OAuth2Providers oAuth2Providers;
    private final JwtProvider jwtProvider;
    private final TransactionTemplate transactionTemplate;
    private final ApplicationEventPublisher applicationEventPublisher;

    public LoginResponse login(LoginRequest loginRequest, String oAuthType) {
        MemberType memberType = MemberType.from(oAuthType);
        OAuth2Provider oAuth2Provider = oAuth2Providers.getProvider(memberType);
        OAuth2MemberResponse oAuth2MemberResponse = oAuth2Provider.getMemberResponse(loginRequest.getAuthCode());

        Member member = memberRepository.findByMemberTypeAndOauthEmail(memberType, oAuth2MemberResponse.getEmail())
                .orElseGet(() -> signUp(oAuth2MemberResponse));

        String accessToken = jwtProvider.createToken(member.getId().toString());
        return LoginResponse.of(accessToken, member);
    }

    private Member signUp(OAuth2MemberResponse oAuth2MemberResponse) {
        Member member = transactionTemplate.execute(action -> memberRepository.save(oAuth2MemberResponse.toMember()));
        applicationEventPublisher.publishEvent(new AdminNotificationEventWithoutTransaction("신규 회원이 가입했습니다🎉", "memberId :" + member.getId().toString()));
        return member;
    }
}
