package com.bookbla.americano.domain.chat.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ChatSubMessage {
    private Long userId;
    private ChatDto chatDto;
    private List<ChatRoomResponse> chatRoomList;
}
