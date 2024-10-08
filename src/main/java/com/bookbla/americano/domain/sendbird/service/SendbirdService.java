package com.bookbla.americano.domain.sendbird.service;

import com.bookbla.americano.domain.sendbird.controller.dto.SendbirdResponse;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import org.openapitools.client.model.*;
import org.sendbird.client.ApiClient;
import org.sendbird.client.ApiException;
import org.sendbird.client.Configuration;
import org.sendbird.client.api.UserApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class SendbirdService {

    private final UserApi userApi;
    private final String apiToken;
    private final MemberRepository memberRepository;

    public SendbirdService(@Value("${sendbird.app.id}") String appId,
                           @Value("${sendbird.api.token}") String apiToken,
                           MemberRepository memberRepository) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://api-" + appId + ".sendbird.com");

        this.userApi = new UserApi(defaultClient);
        this.apiToken = apiToken;
        this.memberRepository = memberRepository;
    }

    public void createUser(Long memberId) throws ApiException {
        Member member = memberRepository.getByIdOrThrow(memberId);
        String userId = member.getId().toString();
        String imageUrl = member.getMemberStyle().getProfileImageType().getImageUrl();
        CreateUserData createUserData = new CreateUserData()
                .userId(userId) //필수
                .nickname(member.getMemberProfile().getName()) // 필수
                .profileUrl(imageUrl)
                ;

        userApi.createUser()
                .apiToken(apiToken)
                .createUserData(createUserData)
                .execute();
    }

    public SendbirdResponse createUserToken(Long memberId) throws ApiException {
        String userId = memberId.toString();
        CreateUserTokenData createUserTokenData = new CreateUserTokenData();
        CreateUserTokenResponse response = userApi.createUserToken(userId)
                .apiToken(apiToken)
                .createUserTokenData(createUserTokenData)
                .execute();

        // Sendbird에서 생성된 토큰을 해당 사용자의 Member 엔티티에 저장
        Member member = memberRepository.getByIdOrThrow(memberId);
        member.updateSendbirdToken(response.getToken());
        return SendbirdResponse.of(member ,response);
    }
}
