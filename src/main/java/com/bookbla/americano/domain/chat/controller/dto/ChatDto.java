package com.bookbla.americano.domain.chat.controller.dto;

import com.bookbla.americano.domain.chat.repository.entity.Chat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ChatDto {

    private Long chatRoomId;

    private Long senderId;

    private Boolean isRead;

    private String content;

    private LocalDateTime sendTime;

    public static ChatDto of(Chat chat) {
        return ChatDto.builder()
                .chatRoomId(chat.getChatRoom().getId())
                .isRead(chat.getIsRead())
                .senderId(chat.getSender().getId())
                .content(chat.getContent())
                .sendTime(chat.getSendTime())
                .build();
    }
}
