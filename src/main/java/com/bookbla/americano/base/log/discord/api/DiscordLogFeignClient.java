package com.bookbla.americano.base.log.discord.api;

import com.bookbla.americano.base.log.discord.api.dto.DiscordWebhookLogMessageRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "discord-logger", url = "${discord.url}")
public interface DiscordLogFeignClient {

    @PostMapping
    void send(@RequestBody DiscordWebhookLogMessageRequest discordWebhookLogMessageRequest);

}
