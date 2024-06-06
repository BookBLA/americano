package com.bookbla.americano.domain.notification.infra.expo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.notification.exception.PushAlarmExceptionType;
import com.bookbla.americano.domain.notification.infra.expo.api.ExpoFeignClient;
import com.bookbla.americano.domain.notification.infra.expo.api.dto.ReceiptsRequest;
import com.bookbla.americano.domain.notification.infra.expo.api.dto.ReceiptsResponse;
import com.bookbla.americano.domain.notification.infra.expo.dto.ExpoNotificationResponse;
import com.bookbla.americano.domain.notification.service.NotificationClient;
import com.bookbla.americano.domain.notification.service.dto.NotificationResponse;
import io.github.jav.exposerversdk.ExpoPushMessage;
import io.github.jav.exposerversdk.ExpoPushTicket;
import io.github.jav.exposerversdk.PushClient;
import io.github.jav.exposerversdk.PushClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Expo reference docs URL: https://docs.expo.dev/push-notifications/sending-notifications/
 *
 * @author iamjooon2
 */

@Component
@RequiredArgsConstructor
public class ExpoNotificationClient implements NotificationClient {

    private static final String EXPO_PUSH_TOKEN_FORMAT = "ExponentPushToken[%s]";
    private static final int EXPO_MAX_MESSAGE_SIZE = 100;

    private final ExpoFeignClient expoFeignClient;

    // https://docs.expo.dev/push-notifications/sending-notifications/
    // Ticket의 Success는 expo 서버에 전달 성공이라는 의미, 사용자에게 전송이 성공했다는 뜻은 아님
    @Override
    public List<NotificationResponse> sendAll(List<String> tokens, String title, String body) {
        List<List<String>> tokenBatches = toTokenBatches(tokens);

        List<ExpoPushTicket> expoPushTickets = new ArrayList<>();
        for (List<String> tokenBatch : tokenBatches) {
            List<String> batch = toExpoTokens(tokenBatch);
            List<ExpoPushMessage> expoPushMessages = setMessages(title, body, batch);

            List<ExpoPushTicket> expoTickets = sendPushAlarmsToExpo(expoPushMessages);

            expoPushTickets.addAll(expoTickets);
        }

        ReceiptsRequest request = ReceiptsRequest.of(expoPushTickets);
        ReceiptsResponse response = expoFeignClient.postReceipts(request);
        return response.getData()
                .entrySet()
                .stream()
                .map(it -> ExpoNotificationResponse.from(it.getKey(), it.getValue()))
                .collect(Collectors.toList());
    }

    // https://docs.expo.dev/push-notifications/sending-notifications/#request-errors
    // 100개 이상 보낼 경우 에러 뜸
    private List<List<String>> toTokenBatches(List<String> tokens) {
        List<List<String>> tokenBatches = new ArrayList<>();
        for (int i = 0; i < tokens.size(); i += EXPO_MAX_MESSAGE_SIZE) {
            int end = Math.min(tokens.size(), i + EXPO_MAX_MESSAGE_SIZE);
            List<String> batch = tokens.subList(i, end);
            tokenBatches.add(new ArrayList<>(batch));
        }
        return tokenBatches;
    }

    private List<String> toExpoTokens(List<String> tokens) {
        return tokens.stream()
                .map(token -> String.format(EXPO_PUSH_TOKEN_FORMAT, token))
                .filter(PushClient::isExponentPushToken)
                .collect(Collectors.toList());
    }

    private List<ExpoPushMessage> setMessages(String title, String body, List<String> expoTokens) {
        ExpoPushMessage expoPushMessage = new ExpoPushMessage();

        for (String exponentPushToken : expoTokens) {
            expoPushMessage.getTo().add(exponentPushToken);
        }
        expoPushMessage.setTitle(title);
        expoPushMessage.setBody(body);

        return Arrays.asList(expoPushMessage);
    }

    private List<ExpoPushTicket> sendPushAlarmsToExpo(List<ExpoPushMessage> expoPushMessages) {
        PushClient client = openClient();

        List<List<ExpoPushMessage>> chunks = client.chunkPushNotifications(expoPushMessages);

        List<CompletableFuture<List<ExpoPushTicket>>> messageRepliesFutures = new ArrayList<>();
        for (List<ExpoPushMessage> chunk : chunks) {
            messageRepliesFutures.add(client.sendPushNotificationsAsync(chunk));
        }

        return getAsyncResponses(messageRepliesFutures);
    }

    // Wait for each completable future to finish
    private List<ExpoPushTicket> getAsyncResponses(List<CompletableFuture<List<ExpoPushTicket>>> messageRepliesFutures) {
        List<ExpoPushTicket> allTickets = new ArrayList<>();
        for (CompletableFuture<List<ExpoPushTicket>> messageReplyFuture : messageRepliesFutures) {
            try {
                allTickets.addAll(messageReplyFuture.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new BaseException(PushAlarmExceptionType.FAIL_TO_SEND_EXPO_SERVER);
            }
        }
        return allTickets;
    }

    @Override
    public List<NotificationResponse> send(String token, String title, String body) {
        String expoToken = toExpoToken(token);
        ExpoPushMessage message = setMessage(title, body, expoToken);

        PushClient client = openClient();
        CompletableFuture<List<ExpoPushTicket>> messageReplyFuture = client.sendPushNotificationsAsync(Arrays.asList(message));

        ExpoPushTicket expoPushTicket = getExpoPushTicket(messageReplyFuture);

        ReceiptsRequest request = ReceiptsRequest.from(expoPushTicket);
        ReceiptsResponse response = expoFeignClient.postReceipts(request);
        return response.getData()
                .entrySet()
                .stream()
                .map(it -> ExpoNotificationResponse.from(it.getKey(), it.getValue()))
                .collect(Collectors.toList());
    }

    private String toExpoToken(String token) {
        String expoToken = String.format(EXPO_PUSH_TOKEN_FORMAT, token);
        if (!PushClient.isExponentPushToken(expoToken)) {
            throw new BaseException(PushAlarmExceptionType.INVALID_EXPO_TOKEN);
        }
        return expoToken;
    }

    // 비동기로 잡아옴..!
    private ExpoPushTicket getExpoPushTicket(CompletableFuture<List<ExpoPushTicket>> messageRepliesFuture) {
        List<ExpoPushTicket> expoPushTickets;
        try {
            expoPushTickets = messageRepliesFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return expoPushTickets.get(0);
    }

    private PushClient openClient() {
        PushClient client;
        try {
            client = new PushClient();
        } catch (PushClientException e) {
            throw new BaseException(PushAlarmExceptionType.INVALID_PUSH_CLIENT);
        }
        return client;
    }

    private ExpoPushMessage setMessage(String title, String body, String expoToken) {
        ExpoPushMessage expoPushMessage = new ExpoPushMessage();
        expoPushMessage.getTo().add(expoToken);
        expoPushMessage.setTitle(title);
        expoPushMessage.setBody(body);
        return expoPushMessage;
    }
}
