package com.bookbla.americano.domain.chat.controller.dto;

import com.bookbla.americano.domain.chat.repository.entity.Chat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ChatSubMessage {

    private Long senderId;
    private String content;
    private Long chatRoomId;
    private LocalDateTime sendTime;
    private Boolean isRead;

    public static ChatSubMessage from(Chat chat) {
        return ChatSubMessage.builder()
                .chatRoomId(chat.getChatRoom().getId())
                .content(chat.getContent())
                .senderId(chat.getSender().getId())
                .sendTime(chat.getSendTime())
                .isRead(chat.getIsRead())
                .build();
    }
}
