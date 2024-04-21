package com.bookbla.americano.domain.fcm.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.fcm.controller.dto.request.FcmMessage.Message;
import com.bookbla.americano.domain.fcm.controller.dto.request.FcmMessage.Notification;
import com.bookbla.americano.domain.fcm.controller.dto.request.FcmTokenCreateRequest;
import com.bookbla.americano.domain.fcm.controller.dto.request.FcmMessage;
import com.bookbla.americano.domain.fcm.controller.dto.response.FcmTokenCreateResponse;
import com.bookbla.americano.domain.fcm.controller.dto.response.FcmTokenDeleteResponse;
import com.bookbla.americano.domain.fcm.exception.FcmExceptionType;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.net.HttpHeaders;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.aspectj.weaver.ast.Not;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FcmService {

    // POST https://fcm.googleapis.com/v1/projects/myproject-b5ae1/messages:send
    private final String API_URL = "https://fcm.googleapis.com/v1/projects/bookbla-2024/messages:send";
    private final ObjectMapper objectMapper;
    private final MemberRepository memberRepository;

    // TODO: TargetToken을 targetUser.getFcmToken으로 변경하기
    public void sendMessageTo(String targetToken, String title, String body) throws IOException {
        String message = makeMessage(targetToken, title, body);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message,
            MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
            .url(API_URL)
            .post(requestBody)
            .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
            .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
            .build();

        Response response = client.newCall(request).execute();
        log.info(response.body().string());
    }

    private String makeMessage(String targetToken, String title, String body)
        throws JsonParseException, JsonProcessingException {

        FcmMessage fcmMessage = FcmMessage.builder()
            .validateOnly(false)
            .message(Message.builder()
                .token(targetToken)
                .notification(Notification.builder()
                    .title(title)
                    .body(body)
                    .image(null)
                    .build())
                .build())
            .build();

        return objectMapper.writeValueAsString(fcmMessage);
    }

    private String getAccessToken() throws IOException {
        final String firebaseConfigPath = "bookbla-2024-firebase.json";

        try {
            GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

            googleCredentials.refreshIfExpired();

            return googleCredentials.getAccessToken().getTokenValue();
        } catch (IOException e) {
            throw new BaseException(FcmExceptionType.ACCESS_TOKEN_FAIL);
        }
    }

    public FcmTokenCreateResponse saveFcmToken(Long memberId, FcmTokenCreateRequest fcmTokenCreateRequest) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        member.updateFcmToken(fcmTokenCreateRequest.getFcmToken());
        return FcmTokenCreateResponse.from(member);
    }

    public FcmTokenDeleteResponse deleteFcmToken(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        member.updateFcmToken(null);
        return FcmTokenDeleteResponse.from(member);
    }
}
