package com.bookbla.americano.domain.sendbird.service;

import com.bookbla.americano.domain.sendbird.controller.dto.request.ChannelRequest;
import com.bookbla.americano.domain.sendbird.controller.dto.response.SendbirdResponse;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import org.openapitools.client.model.*;
import org.sendbird.client.ApiClient;
import org.sendbird.client.ApiException;
import org.sendbird.client.Configuration;
import org.sendbird.client.api.GroupChannelApi;
import org.sendbird.client.api.MetadataApi;
import org.sendbird.client.api.UserApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SendbirdService {

    private static final String CHANNEL_TYPE = "groupChannel";
    private static final String ACCEPT_STATUS = "yet";


    private final UserApi userApi;
    private final GroupChannelApi groupChannelApi;
    private final MetadataApi metadataApi;
    private final String apiToken;
    private final MemberRepository memberRepository;


    public SendbirdService(@Value("${sendbird.app.id}") String appId,
                           @Value("${sendbird.api.token}") String apiToken,
                           MemberRepository memberRepository) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://api-" + appId + ".sendbird.com");

        this.apiToken = apiToken;
        this.userApi = new UserApi(defaultClient);
        this.groupChannelApi = new GroupChannelApi(defaultClient);
        this.metadataApi = new MetadataApi(defaultClient);
        this.memberRepository = memberRepository;
    }

    public SendbirdResponse createOrView(Long memberId) throws ApiException {
        Member member = memberRepository.getByIdOrThrow(memberId);
        String userId = member.getId().toString();

        String existingToken = member.getSendbirdToken();

        if (existingToken != null && !existingToken.isEmpty()) {
            return SendbirdResponse.of(member, existingToken);
        }

        try {
            userApi.viewUserById(userId)
                    .apiToken(apiToken)
                    .execute();
        } catch (ApiException e) {
            // USER_NOT_FOUND 에러코드 400301
            if (e.getCode() == 400301) {
                createUser(member);
                return createUserToken(member);
            } else {
                throw e;
            }
        }

        return createUserToken(member);
    }

    private void createUser(Member member) throws ApiException {
        String userId = member.getId().toString();
        String imageUrl = member.getMemberStyle().getProfileImageType().getImageUrl();
        CreateUserData createUserData = new CreateUserData()
                .userId(userId) //필수
                .nickname(member.getMemberProfile().getName()) // 필수
                .profileUrl(imageUrl);

        userApi.createUser()
                .apiToken(apiToken)
                .createUserData(createUserData)
                .execute();
    }

    private SendbirdResponse createUserToken(Member member) throws ApiException {
        String userId = member.getId().toString();
        CreateUserTokenData createUserTokenData = new CreateUserTokenData();
        CreateUserTokenResponse response = userApi.createUserToken(userId)
                .apiToken(apiToken)
                .createUserTokenData(createUserTokenData)
                .execute();

        // Sendbird에서 생성된 토큰을 해당 사용자의 Member 엔티티에 저장
        member.updateSendbirdToken(response.getToken());
        return SendbirdResponse.of(member, response.getToken());
    }

    public void updateSendbirdNickname(Long memberId, String newNickname) {
        try {
            Member member = memberRepository.getByIdOrThrow(memberId);
            String userId = member.getId().toString();

            UpdateUserByIdData updateUserByIdData = new UpdateUserByIdData()
                    .nickname(newNickname);

            userApi.updateUserById(userId)
                    .apiToken(apiToken)
                    .updateUserByIdData(updateUserByIdData)
                    .execute();
        } catch (ApiException e) {
            throw new RuntimeException("Sendbird 유저 닉네임 업데이트 실패: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while updating Sendbird user", e);
        }
    }

    public void updateSendbirdProfileUrl(Long memberId, String newProfileUrl) {
        try {

            Member member = memberRepository.getByIdOrThrow(memberId);
            String userId = member.getId().toString();

            UpdateUserByIdData updateUserByIdData = new UpdateUserByIdData()
                    .profileUrl(newProfileUrl);

            userApi.updateUserById(userId)
                    .apiToken(apiToken)
                    .updateUserByIdData(updateUserByIdData)
                    .execute();
        } catch (ApiException e) {
            throw new RuntimeException("Sendbird 유저 프로필 사진 업데이트 실패: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while updating Sendbird user", e);
        }
    }

    public boolean isUserExists(Long memberId) throws ApiException {
        String userId = memberId.toString();  // memberId를 userId로 사용

        // Sendbird에서 해당 userId로 유저 조회
        userApi.viewUserById(userId)
                .apiToken(apiToken)
                .execute();
        // 조회가 성공하면 유저가 존재함
        return true;
    }

    public void createSendbirdGroupChannel(ChannelRequest request) {
        GcCreateChannelData channelData = new GcCreateChannelData();

        List<String> userIds = new ArrayList<>();
        userIds.add(request.getSendMemberId().toString());
        userIds.add(request.getTargetMemberId().toString());

        channelData.setUserIds(userIds);
        channelData.setIsDistinct(true);
        channelData.setIsPublic(false);

        try {
            groupChannelApi.gcCreateChannel()
                    .apiToken(apiToken)
                    .gcCreateChannelData(channelData)
                    .execute();
        } catch (ApiException e) {
            throw new RuntimeException("Sendbird 그룹 채널 생성 에러: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while creating Sendbird GroupChannel", e);
        }
    }

    public void createSendbirdMetadata(ChannelRequest request) {

        Map<String, String> metadata = new HashMap<>();
        metadata.put("sendMemberId", request.getSendMemberId().toString());
        metadata.put("sendMemberName", request.getSendMemberName());
        metadata.put("targetMemberId", request.getTargetMemberId().toString());
        metadata.put("targetMemberBookId", request.getTargetMemberBookId().toString());
        metadata.put("acceptStatus", ACCEPT_STATUS);

        CreateChannelMetadataData createChannelMetadataData = new CreateChannelMetadataData()
                .metadata(metadata);

        try {
            metadataApi.createChannelMetadata(CHANNEL_TYPE, request.getChannelUrl())
                    .apiToken(apiToken)
                    .createChannelMetadataData(createChannelMetadataData)
                    .execute();
        } catch (ApiException e) {
            throw new RuntimeException("Sendbird 메타데이터 생성 에러: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while creating Sendbird Metadata", e);
        }
    }
}
