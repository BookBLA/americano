package com.bookbla.americano.domain.chat.service;

import com.bookbla.americano.domain.chat.controller.dto.ChatDto;
import com.bookbla.americano.domain.chat.controller.dto.ChatPubMessage;
import com.bookbla.americano.domain.chat.controller.dto.RoomConnection;
import com.bookbla.americano.domain.chat.repository.entity.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatService {

    Page<ChatDto> getChatList(Long roomId, Pageable pageable);

    Chat save(ChatPubMessage chatDto);

    void readChatAll(Long roomId, Long memberId);

    void handleSendChat(ChatPubMessage chatDto);

    void handleMemberRoomConnect(Long roomId, Long memberId, RoomConnection connection);
}
