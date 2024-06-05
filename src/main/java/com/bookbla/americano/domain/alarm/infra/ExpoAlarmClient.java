package com.bookbla.americano.domain.alarm.infra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.alarm.exception.PushAlarmExceptionType;
import com.bookbla.americano.domain.alarm.infra.dto.ExpoAlarmResponse;
import com.bookbla.americano.domain.alarm.service.AlarmClient;
import com.bookbla.americano.domain.alarm.service.dto.AlarmResponse;
import io.github.jav.exposerversdk.ExpoPushMessage;
import io.github.jav.exposerversdk.ExpoPushTicket;
import io.github.jav.exposerversdk.PushClient;
import io.github.jav.exposerversdk.PushClientException;
import org.springframework.stereotype.Component;

@Component
public class ExpoAlarmClient implements AlarmClient {

    private static final String EXPO_PUSH_TOKEN_FORMAT = "ExponentPushToken[%s]";

    // https://docs.expo.dev/push-notifications/sending-notifications/#request-errors
    @Override
    public List<AlarmResponse> sendAll(List<String> tokens, String title, String body) {
        List<List<String>> tokenBatches = toTokenBatches(tokens);

        List<ExpoPushTicket> expoPushTickets = new ArrayList<>();
        for (List<String> tokenBatch : tokenBatches) {
            List<String> expoTokens = toExpoToken(tokenBatch);
            List<ExpoPushMessage> expoPushMessages = setMessages(title, body, expoTokens);

            List<ExpoPushTicket> expoTickets = sendPushAlarmsToExpo(expoPushMessages);

            expoPushTickets.addAll(expoTickets);
        }
        return expoPushTickets.stream()
                .map(ExpoAlarmResponse::from)
                .collect(Collectors.toList());
    }

    private List<List<String>> toTokenBatches(List<String> tokens) {
        List<List<String>> tokenBatches = new ArrayList<>();
        for (int i = 0; i < tokens.size(); i += 100) {
            int end = Math.min(tokens.size(), i + 100);
            List<String> batch = tokens.subList(i, end);
            tokenBatches.add(new ArrayList<>(batch)); // Add the batch to the list of batches
        }
        return tokenBatches;
    }

    private List<String> toExpoToken(List<String> tokens) {
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

        // Wait for each completable future to finish
        return getAsyncResponses(messageRepliesFutures);
    }

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
    public AlarmResponse send(String token, String title, String body) {
        String expoToken = toExpoToken(token);
        ExpoPushMessage message = setMessage(title, body, expoToken);

        PushClient client = openClient();
        CompletableFuture<List<ExpoPushTicket>> messageReplyFuture = client.sendPushNotificationsAsync(Arrays.asList(message));

        ExpoPushTicket expoPushTicket = getExpoPushTicket(messageReplyFuture);
        return ExpoAlarmResponse.of(expoPushTicket, token);
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
