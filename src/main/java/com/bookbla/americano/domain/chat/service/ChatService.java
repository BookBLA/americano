package com.bookbla.americano.domain.chat.service;

import com.bookbla.americano.domain.chat.controller.dto.ChatDto;
import com.bookbla.americano.domain.chat.repository.entity.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatService {

    Page<ChatDto> getChatList(Long roomId, Pageable pageable);

    Chat save(ChatDto chatDto);
}
