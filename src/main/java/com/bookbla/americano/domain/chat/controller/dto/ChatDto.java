package com.bookbla.americano.domain.chat.controller.dto;

import com.bookbla.americano.domain.chat.repository.entity.Chat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.joda.time.DateTime;

@Getter
@Builder
@AllArgsConstructor
public class ChatDto {

    private Long chatRoomId;

    private Long senderId;

    private String content;

    private DateTime sendTime;

    public static ChatDto of(Chat chat) {
        return ChatDto.builder()
                .chatRoomId(chat.getChatRoom().getId())
                .senderId(chat.getSender().getId())
                .content(chat.getContent())
                .sendTime(chat.getSendTime())
                .build();
    }
}
