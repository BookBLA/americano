package com.bookbla.americano.domain.chat.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.chat.controller.dto.ChatDto;
import com.bookbla.americano.domain.chat.repository.entity.Chat;
import com.bookbla.americano.domain.chat.service.ChatService;
import com.bookbla.americano.domain.notification.service.AlarmService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final AlarmService alarmService;

    @MessageMapping("/ws/{memberId}/{targetId}")
    @SendTo("/ws/{targetId}")
    public ChatDto sendMessage(ChatDto chat) {
        Chat result = chatService.save(chat);
        alarmService.sendPushAlarmForChat(result);
        return chat;
    }



    @GetMapping
    public ResponseEntity<Page<ChatDto>> getChatList(
            @Parameter(hidden = true) @User LoginUser loginUser,
            @RequestParam Long roomId,
            Pageable pageable) {
       Page<ChatDto> result = chatService.getChatList(roomId, pageable);
       return ResponseEntity.ok(result);
    }
}
