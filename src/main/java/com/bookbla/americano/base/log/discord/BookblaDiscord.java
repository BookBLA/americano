package com.bookbla.americano.base.log.discord;

import com.bookbla.americano.base.log.discord.dto.DiscordWebhookMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookblaDiscord {

    private final DiscordFeignClient discordFeignClient;

    public void sendMessage(String message) {
        DiscordWebhookMessageRequest dto = new DiscordWebhookMessageRequest(message);
        discordFeignClient.send(dto);
    }
}

