package com.bookbla.americano.domain.chat.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChatPubMessage {
    private Long senderId;
    private String content;
    private Long roomId;
    // Getters and Setters
}
