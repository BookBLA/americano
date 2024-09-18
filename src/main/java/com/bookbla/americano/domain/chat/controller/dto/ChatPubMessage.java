package com.bookbla.americano.domain.chat.controller.dto;

import com.bookbla.americano.domain.chat.repository.entity.Chat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ChatPubMessage {
    private Long senderId;
    private String content;
    private Long chatRoomId;

}
