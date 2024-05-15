package com.bookbla.americano.base.log.discord;

import com.bookbla.americano.base.log.discord.dto.DiscordWebhookMessageRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "discord", url = "${discord.url}")
public interface DiscordFeignClient {

    @PostMapping
    void send(@RequestBody DiscordWebhookMessageRequest discordWebhookMessageRequest);

}
