package com.bookbla.americano.domain.alarm.infra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.alarm.exception.PushAlarmExceptionType;
import com.bookbla.americano.domain.alarm.service.AlarmClient;
import com.bookbla.americano.domain.alarm.service.dto.AlarmSendResponse;
import io.github.jav.exposerversdk.ExpoPushMessage;
import io.github.jav.exposerversdk.ExpoPushTicket;
import io.github.jav.exposerversdk.PushClient;
import io.github.jav.exposerversdk.PushClientException;
import org.springframework.stereotype.Component;

@Component
public class ExpoAlarmClient implements AlarmClient {

    private static final String EXPO_PUSH_TOKEN_FORMAT = "ExponentPushToken[%s]";

    @Override
    public List<AlarmSendResponse> send(String token, String title, String body) {
        String expoToken = String.format(EXPO_PUSH_TOKEN_FORMAT, token);
        if (!PushClient.isExponentPushToken(expoToken)) {
            throw new BaseException(PushAlarmExceptionType.INVALID_EXPO_TOKEN);
        }

        ExpoPushMessage message = setMessage(title, body, expoToken);

        PushClient client = openPushClient();
        List<List<ExpoPushMessage>> chunks = client.chunkPushNotifications(Arrays.asList(message));
        List<CompletableFuture<List<ExpoPushTicket>>> messageRepliesFutures = chunks.stream()
                .map(client::sendPushNotificationsAsync)
                .collect(Collectors.toList());

        List<ExpoPushTicket> results = getExpoPushTickets(messageRepliesFutures);
        return results.stream()
                .map(it -> new AlarmSendResponse(it.getStatus().toString(), title, body, token))
                .collect(Collectors.toList());
    }

    // Wait for each completable future to finish
    private List<ExpoPushTicket> getExpoPushTickets(List<CompletableFuture<List<ExpoPushTicket>>> messageRepliesFutures) {
        List<ExpoPushTicket> results = new ArrayList<>();
        for (CompletableFuture<List<ExpoPushTicket>> messageReplyFuture : messageRepliesFutures) {
            try {
                results.addAll(messageReplyFuture.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new BaseException(PushAlarmExceptionType.FAIL_TO_SEND_EXPO_SERVER, e);
            }
        }
        return results;
    }

    private PushClient openPushClient() {
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
