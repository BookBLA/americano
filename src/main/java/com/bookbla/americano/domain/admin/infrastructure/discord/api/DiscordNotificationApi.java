package com.bookbla.americano.domain.admin.infrastructure.discord.api;

import com.bookbla.americano.domain.admin.infrastructure.discord.api.dto.AdminDiscordNotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "discord-notifiaction", url = "${discord.notification-url}")
public interface DiscordNotificationApi {

    @PostMapping
    void send(@RequestBody AdminDiscordNotificationRequest request);

}
