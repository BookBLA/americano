package com.bookbla.americano.domain.chat.service.impl;

import com.bookbla.americano.domain.chat.controller.dto.ChatDto;
import com.bookbla.americano.domain.chat.repository.ChatRepository;
import com.bookbla.americano.domain.chat.repository.entity.Chat;
import com.bookbla.americano.domain.chat.repository.entity.ChatRoom;
import com.bookbla.americano.domain.chat.service.ChatService;
import com.bookbla.americano.domain.member.repository.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final EntityManager em;

    public Page<ChatDto> getChatList(Long roomId, Pageable pageable) {
        Page<Chat> chats = chatRepository.findByChatRoom_Id(roomId, pageable);

        return chats.map(ChatDto::of);
    }

    public Chat save(ChatDto chatDto) {
        Chat chat = Chat.builder()
                .sendTime(chatDto.getSendTime())
                .content(chatDto.getContent())
                .sender(em.getReference(Member.class, chatDto.getSenderId()))
                .chatRoom(em.getReference(ChatRoom.class, chatDto.getChatRoomId()))
                .build();
        return chatRepository.save(chat);
    }
}
