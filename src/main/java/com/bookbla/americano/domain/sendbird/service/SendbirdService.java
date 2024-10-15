package com.bookbla.americano.domain.sendbird.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.book.repository.entity.Book;
import com.bookbla.americano.domain.member.exception.MemberBookExceptionType;
import com.bookbla.americano.domain.member.repository.MemberBookRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.postcard.controller.dto.response.PostcardReadResponse;
import com.bookbla.americano.domain.sendbird.controller.dto.response.SendbirdResponse;
import org.openapitools.client.model.*;
import org.sendbird.client.ApiClient;
import org.sendbird.client.ApiException;
import org.sendbird.client.Configuration;
import org.sendbird.client.api.GroupChannelApi;
import org.sendbird.client.api.MessageApi;
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

    private static final String CHANNEL_TYPE = "group_channels";
    private static final String ACCEPT_STATUS = "yet";


    private final UserApi userApi;
    private final String apiToken;
    private final GroupChannelApi groupChannelApi;
    private final MetadataApi metadataApi;
    private final MessageApi messageApi;
    private final MemberRepository memberRepository;
    private final MemberBookRepository memberBookRepository;


    public SendbirdService(@Value("${sendbird.app.id}") String appId,
                           @Value("${sendbird.api.token}") String apiToken,
                           MemberRepository memberRepository,
                           MemberBookRepository memberBookRepository) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://api-" + appId + ".sendbird.com");

        this.apiToken = apiToken;
        this.userApi = new UserApi(defaultClient);
        this.groupChannelApi = new GroupChannelApi(defaultClient);
        this.metadataApi = new MetadataApi(defaultClient);
        this.messageApi = new MessageApi(defaultClient);
        this.memberRepository = memberRepository;
        this.memberBookRepository = memberBookRepository;
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

    public String createSendbirdGroupChannel(PostcardReadResponse postcardReadResponse) {
        GcCreateChannelData channelData = new GcCreateChannelData();

        List<String> userIds = new ArrayList<>();
        userIds.add(postcardReadResponse.getSendMemberId().toString());
        userIds.add(postcardReadResponse.getReceiveMemberId().toString());

        channelData.setUserIds(userIds);
        channelData.setIsDistinct(true);
        channelData.setIsPublic(false);

        try {
            SendBirdGroupChannel groupChannel = groupChannelApi.gcCreateChannel()
                    .apiToken(apiToken)
                    .gcCreateChannelData(channelData)
                    .execute();
            return groupChannel.getChannelUrl();
        } catch (ApiException e) {
            throw new RuntimeException("Sendbird 그룹 채널 생성 에러: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while creating Sendbird GroupChannel", e);
        }
    }

    public void createSendbirdMetadata(PostcardReadResponse postcardReadResponse, String channelUrl){

        Map<String, String> metadata = new HashMap<>();
        metadata.put("sendMemberId", postcardReadResponse.getSendMemberId().toString());
        metadata.put("sendMemberName", postcardReadResponse.getSendMemberName());
        metadata.put("targetMemberId", postcardReadResponse.getReceiveMemberId().toString());
        metadata.put("targetMemberBookId", postcardReadResponse.getReceiveMemberBookId().toString());
        metadata.put("acceptStatus", ACCEPT_STATUS);

        CreateChannelMetadataData createChannelMetadataData = new CreateChannelMetadataData()
                .metadata(metadata);

        try {
            metadataApi.createChannelMetadata(CHANNEL_TYPE, channelUrl)
                    .apiToken(apiToken)
                    .createChannelMetadataData(createChannelMetadataData)
                    .execute();
        } catch (ApiException e) {
            deleteSendbirdGroupChannel(channelUrl);
            throw new RuntimeException("Sendbird 메타데이터 생성 에러: " + e.getMessage(), e);
        } catch (Exception e) {
            deleteSendbirdGroupChannel(channelUrl);
            throw new RuntimeException("Unexpected error while creating Sendbird Metadata", e);
        }
    }

    public void sendEntryMessage(PostcardReadResponse postcardReadResponse, String channelUrl) {
        Book book = memberBookRepository.findBookById(postcardReadResponse.getReceiveMemberBookId())
                .orElseThrow(() -> new BaseException(MemberBookExceptionType.BOOK_NOT_FOUND));

        String userId = postcardReadResponse.getSendMemberId().toString();

        SendMessageData bookTitleMessage = new SendMessageData()
                .userId(userId)
                .message("《" + book.getTitle() + "》")
                .messageType("MESG");   // 일반 메시지

        SendMessageData replyMessage = new SendMessageData()
                .userId(userId)
                .message(postcardReadResponse.getMemberReply())
                .messageType("MESG");

        try {
            messageApi.sendMessage(CHANNEL_TYPE, channelUrl)
                    .apiToken(apiToken)
                    .sendMessageData(bookTitleMessage)
                    .execute();

            messageApi.sendMessage(CHANNEL_TYPE, channelUrl)
                    .apiToken(apiToken)
                    .sendMessageData(replyMessage)
                    .execute();
        } catch (ApiException e) {
            deleteSendbirdGroupChannel(channelUrl);
            throw new RuntimeException("Sendbird 엽서 인사말 보내기 실패: "+ e.getMessage(), e);
        } catch (Exception e) {
            deleteSendbirdGroupChannel(channelUrl);
            throw new RuntimeException("Unexpected error while sending Sendbird Message", e);
        }
    }

    public void deleteSendbirdGroupChannel(String channelUrl) {
        try {
            groupChannelApi.gcDeleteChannelByUrl(channelUrl)
                    .apiToken(apiToken)
                    .execute();
        } catch (ApiException e) {
            throw new RuntimeException("Sendbird 그룹 채널 삭제 에러: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while delete Sendbird GroupChannel", e);
        }
    }
}
