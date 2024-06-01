package com.bookbla.americano.domain.auth.service.impl;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.base.jwt.JwtProvider;
import com.bookbla.americano.domain.auth.controller.dto.request.LoginRequest;
import com.bookbla.americano.domain.auth.controller.dto.request.RejoinRequest;
import com.bookbla.americano.domain.auth.controller.dto.response.LoginResponse;
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
import org.springframework.transaction.support.TransactionTemplate;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;
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

        // 회원이 탈퇴 상태라면 예외
        member.validateDeleted();

        String accessToken = jwtProvider.createToken(member.getId().toString());
        return LoginResponse.of(accessToken, member);
    }

    private Member signUp(OAuth2MemberResponse oAuth2MemberResponse) {
        return transactionTemplate.execute(
                action -> memberRepository.save(oAuth2MemberResponse.toMember()));
    }

    @Override
    public RejoinResponse rejoin(RejoinRequest rejoinRequest, String oAuthType) {
        MemberType memberType = MemberType.from(oAuthType);
        OAuth2Provider oAuth2Provider = oAuth2Providers.getProvider(memberType);
        OAuth2MemberResponse oAuth2MemberResponse = oAuth2Provider.getMemberResponse(
                rejoinRequest.getAuthCode());

        Member member = memberRepository.findByMemberTypeAndOauthEmail(memberType,
                        oAuth2MemberResponse.getEmail())
                .orElseGet(() -> signUp(oAuth2MemberResponse));

        LocalDateTime deleteAtThirtyDaysAfter = member.getDeleteAt().minusDays(30);

        if (member.getMemberStatus() == MemberStatus.DELETED) {
            // 회원 탈퇴 한 뒤에 30일이 지났다면
            if (LocalDateTime.now().isAfter(deleteAtThirtyDaysAfter)) {
                transactionTemplate.executeWithoutResult(
                        action -> member.updateMemberStatus(MemberStatus.COMPLETED,
                                LocalDateTime.now()));
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
