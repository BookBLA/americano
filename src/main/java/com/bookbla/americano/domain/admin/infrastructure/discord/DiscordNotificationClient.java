package com.bookbla.americano.domain.admin.infrastructure.discord;

import com.bookbla.americano.domain.admin.service.AdminNotificationClient;
import com.bookbla.americano.domain.admin.infrastructure.discord.api.DiscordNotificationApi;
import com.bookbla.americano.domain.admin.infrastructure.discord.api.dto.AdminDiscordNotificationRequest;
import lombok.RequiredArgsConstructor;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DiscordNotificationClient implements AdminNotificationClient {

    private static final int DISCORD_MAX_MESSAGE_INCLUSIVE = 2_000;
    private static final String MESSAGE_FORMAT = "**[새로운 이벤트가 생성됐습니다]**\n\n" +
            "*[발생 시간]*\n%s\n\n" +
            "*[이벤트 내역]*\n%s\n\n" +
            "*[세부 내역]*\n%s\n\n";

    private final DiscordNotificationApi api;

    @Override
    public void send(String title, String contents) {
        String message = toMessage(title, contents);
        AdminDiscordNotificationRequest request = new AdminDiscordNotificationRequest(message);
        api.send(request);
    }

    private String toMessage(String title, String contents) {
        String message = String.format(MESSAGE_FORMAT, LocalDate.now(), title, contents);
        return isLongerThanOrEqualMaxMessageSize(message)
                ? message.substring(0, DISCORD_MAX_MESSAGE_INCLUSIVE - 1)
                : message;
    }

    private boolean isLongerThanOrEqualMaxMessageSize(String message) {
        return message.length() >= DISCORD_MAX_MESSAGE_INCLUSIVE;
    }
}
