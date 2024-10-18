package com.bookbla.americano.domain.sendbird.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.book.repository.entity.Book;
import com.bookbla.americano.domain.member.exception.MemberBookExceptionType;
import com.bookbla.americano.domain.member.repository.MemberBookRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.postcard.controller.dto.response.PostcardReadResponse;
import com.bookbla.americano.domain.sendbird.controller.dto.response.SendbirdResponse;
import com.bookbla.americano.domain.sendbird.exception.SendbirdException;
import org.jetbrains.annotations.NotNull;
import org.openapitools.client.model.*;
import org.sendbird.client.ApiClient;
import org.sendbird.client.ApiException;
import org.sendbird.client.Configuration;
import org.sendbird.client.api.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SendbirdService {

    // TODO: SendbirdService는 센드버드에 관련된 역할만 하도록 변경
    private static final int USER_NOT_FOUND = 400;
    private static final String CHANNEL_TYPE = "group_channels";
    private static final String ACCEPT_STATUS = "yet";
    private static final String MESSAGE_TYPE = "MESG";


    private final UserApi sendbirdUserApi;
    private final String apiToken;
    private final GroupChannelApi groupChannelApi;
    private final MetadataApi metadataApi;
    private final MessageApi messageApi;
    private final ModerationApi moderationApi;
    private final MemberRepository memberRepository;
    private final MemberBookRepository memberBookRepository;


    public SendbirdService(@Value("${sendbird.app.id}") String appId,
                           @Value("${sendbird.api.token}") String apiToken,
                           MemberRepository memberRepository,
                           MemberBookRepository memberBookRepository) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://api-" + appId + ".sendbird.com");

        this.apiToken = apiToken;
        this.sendbirdUserApi = new UserApi(defaultClient);
        this.groupChannelApi = new GroupChannelApi(defaultClient);
        this.metadataApi = new MetadataApi(defaultClient);
        this.messageApi = new MessageApi(defaultClient);
        this.moderationApi = new ModerationApi(defaultClient);
        this.memberRepository = memberRepository;
        this.memberBookRepository = memberBookRepository;
    }

    public SendbirdResponse createOrView(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        String userId = member.getId().toString();

        String existingToken = member.getSendbirdToken();

        if (existingToken != null && !existingToken.isEmpty()) {
            return SendbirdResponse.of(member, existingToken);
        }

        try {
            sendbirdUserApi.viewUserById(userId)
                    .apiToken(apiToken)
                    .execute();
            updateSendbirdNickname(memberId, member.getMemberProfile().getName());
            updateSendbirdProfileUrl(memberId, member.getMemberStyle().getProfileImageType().getImageUrl());
            return createSendbirdUserToken(member);
        } catch (ApiException e) {
            if (e.getCode() == USER_NOT_FOUND) {
                createSendbirdUser(member);
                return createSendbirdUserToken(member);
            } else {
                throw new SendbirdException(e);
            }
        }
    }

    private void createSendbirdUser(Member member) {
        String userId = member.getId().toString();
        String imageUrl = member.getMemberStyle().getProfileImageType().getImageUrl();
        CreateUserData createUserData = new CreateUserData()
                .userId(userId) //필수
                .nickname(member.getMemberProfile().getName()) // 필수
                .profileUrl(imageUrl);

        try {
            sendbirdUserApi.createUser()
                    .apiToken(apiToken)
                    .createUserData(createUserData)
                    .execute();
        } catch (ApiException e) {
            throw new SendbirdException(e);
        }
    }

    private SendbirdResponse createSendbirdUserToken(Member member) {
        String userId = member.getId().toString();
        CreateUserTokenData createUserTokenData = new CreateUserTokenData();

        try {
            CreateUserTokenResponse response = sendbirdUserApi.createUserToken(userId)
                    .apiToken(apiToken)
                    .createUserTokenData(createUserTokenData)
                    .execute();
            member.updateSendbirdToken(response.getToken());
            return SendbirdResponse.of(member, response.getToken());
        } catch (ApiException e) {
            throw new SendbirdException(e);
        }
    }

    public void updateSendbirdNickname(Long memberId, String newNickname) {
        try {
            Member member = memberRepository.getByIdOrThrow(memberId);
            String userId = member.getId().toString();

            UpdateUserByIdData updateUserByIdData = new UpdateUserByIdData()
                    .nickname(newNickname);

            sendbirdUserApi.updateUserById(userId)
                    .apiToken(apiToken)
                    .updateUserByIdData(updateUserByIdData)
                    .execute();
        } catch (ApiException e) {
            throw new SendbirdException(e);
        }
    }

    public void updateSendbirdProfileUrl(Long memberId, String newProfileUrl) {
        try {

            Member member = memberRepository.getByIdOrThrow(memberId);
            String userId = member.getId().toString();

            UpdateUserByIdData updateUserByIdData = new UpdateUserByIdData()
                    .profileUrl(newProfileUrl);

            sendbirdUserApi.updateUserById(userId)
                    .apiToken(apiToken)
                    .updateUserByIdData(updateUserByIdData)
                    .execute();
        } catch (ApiException e) {
            throw new SendbirdException(e);
        }
    }

    public boolean isUserExists(Long memberId) throws ApiException {
        String userId = memberId.toString();  // memberId를 userId로 사용

        // Sendbird에서 해당 userId로 유저 조회
        sendbirdUserApi.viewUserById(userId)
                .apiToken(apiToken)
                .execute();
        // 조회가 성공하면 유저가 존재함
        return true;
    }

    public String createSendbirdGroupChannel(PostcardReadResponse postcardReadResponse) {
        GcCreateChannelData channelData = new GcCreateChannelData();

        List<String> userIds = List.of(
                postcardReadResponse.getSendMemberId().toString(),
                postcardReadResponse.getReceiveMemberId().toString());

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
            throw new SendbirdException(e);
        }
    }

    public void createSendbirdMetadata(PostcardReadResponse postcardReadResponse, String channelUrl) {

        Map<String, String> sendbirdMetadata = createMetadata(postcardReadResponse);
        CreateChannelMetadataData createChannelMetadataData = new CreateChannelMetadataData().metadata(sendbirdMetadata);

        try {
            metadataApi.createChannelMetadata(CHANNEL_TYPE, channelUrl)
                    .apiToken(apiToken)
                    .createChannelMetadataData(createChannelMetadataData)
                    .execute();
        } catch (ApiException e) {
            deleteSendbirdGroupChannel(channelUrl);
            throw new SendbirdException(e);
        }
    }

    @NotNull
    private Map<String, String> createMetadata(PostcardReadResponse postcardReadResponse) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("postcardId", postcardReadResponse.getPostcardId().toString());
        metadata.put("sendMemberId", postcardReadResponse.getSendMemberId().toString());
        metadata.put("sendMemberName", postcardReadResponse.getSendMemberName());
        metadata.put("targetMemberId", postcardReadResponse.getReceiveMemberId().toString());
        metadata.put("acceptStatus", ACCEPT_STATUS);
        return metadata;
    }

    public Long getSendbirdMetadata(String channelUrl){
        try {
            Map<String, String> postcardId = metadataApi.viewChannelMetadataByKey(CHANNEL_TYPE, channelUrl, "postcardId")
                    .apiToken(apiToken)
                    .execute();
            return Long.valueOf(postcardId.get("postcardId"));
        } catch (ApiException e) {
            throw new SendbirdException(e);
        }
    }


    public void sendEntryMessage(PostcardReadResponse postcardReadResponse, String channelUrl) {
        Book book = memberBookRepository.findBookById(postcardReadResponse.getReceiveMemberBookId())
                .orElseThrow(() -> new BaseException(MemberBookExceptionType.BOOK_NOT_FOUND));

        String userId = postcardReadResponse.getSendMemberId().toString();

        SendMessageData bookTitleMessage = new SendMessageData()
                .userId(userId)
                .message("《" + book.getTitle() + "》")
                .messageType(MESSAGE_TYPE);

        SendMessageData replyMessage = new SendMessageData()
                .userId(userId)
                .message(postcardReadResponse.getMemberReply())
                .messageType(MESSAGE_TYPE);

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
            throw new SendbirdException(e);
        }
    }

    public void freezeSendbirdGroupChannel(String channelUrl) {
        GcFreezeChannelData freezeChannelData = new GcFreezeChannelData();
        freezeChannelData.freeze(true);
        freezeChannelData.channelUrl(channelUrl);

        try {
            moderationApi.gcFreezeChannel(channelUrl)
                    .apiToken(apiToken)
                    .gcFreezeChannelData(freezeChannelData)
                    .execute();
        } catch (ApiException e) {
            deleteSendbirdGroupChannel(channelUrl);
            throw new SendbirdException(e);
        }
    }

    public void unFreezeSendbirdGroupChannel(String channelUrl) {
        GcFreezeChannelData unFreezeChannelData = new GcFreezeChannelData();
        unFreezeChannelData.freeze(false);
        unFreezeChannelData.channelUrl(channelUrl);

        try {
            moderationApi.gcFreezeChannel(channelUrl)
                    .apiToken(apiToken)
                    .gcFreezeChannelData(unFreezeChannelData)
                    .execute();
        } catch (ApiException e) {
            throw new SendbirdException(e);
        }
    }

    public void deleteSendbirdGroupChannel(String channelUrl) {
        try {
            groupChannelApi.gcDeleteChannelByUrl(channelUrl)
                    .apiToken(apiToken)
                    .execute();
        } catch (ApiException e) {
            throw new SendbirdException(e);

        }
    }
}
