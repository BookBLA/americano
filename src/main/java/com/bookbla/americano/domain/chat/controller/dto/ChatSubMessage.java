package com.bookbla.americano.domain.chat.controller.dto;

import com.bookbla.americano.domain.chat.enums.SendChatStatus;
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
    private SendChatStatus status;

    public void setStatus(SendChatStatus status) {
        this.status = status;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public static ChatSubMessage from(Chat chat) {
        return ChatSubMessage.builder()
                .chatRoomId(chat.getChatRoom().getId())
                .content(chat.getContent())
                .senderId(chat.getSender().getId())
                .sendTime(chat.getSendTime())
                .isRead(chat.getIsRead())
                .build();
    }
    public static ChatSubMessage from(ChatPubMessage pub) {
        return ChatSubMessage.builder()
                .chatRoomId(pub.getChatRoomId())
                .content(pub.getContent())
                .senderId(pub.getSenderId())
                .sendTime(pub.getSendTime())
                .build();
    }
}
