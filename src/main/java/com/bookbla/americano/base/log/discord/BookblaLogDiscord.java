package com.bookbla.americano.base.log.discord;

import com.bookbla.americano.base.log.discord.api.DiscordLogFeignClient;
import com.bookbla.americano.base.log.discord.api.dto.DiscordWebhookLogMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookblaLogDiscord {

    private final DiscordLogFeignClient discordLogFeignClient;

    public void sendMessage(String message) {
        DiscordWebhookLogMessageRequest dto = new DiscordWebhookLogMessageRequest(message);
        discordLogFeignClient.send(dto);
    }
}

