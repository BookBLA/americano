package com.bookbla.americano.domain.chat.service;

import com.bookbla.americano.domain.chat.controller.dto.ChatRoomResponse;
import com.bookbla.americano.domain.chat.repository.entity.ChatRoom;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.postcard.repository.entity.Postcard;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatRoomService {  // 단위 테스트를 위한 interface
    
    // Postcard 외 다른 Entity 를 활용하여 ChatRoom 을 생성해야하면 Method Overloading 으로 생성하는 게 좋을듯
    // ex) ChatRoom createChatRoom(List<Member> members, Book book);
    ChatRoom createChatRoom(List<Member> members, Postcard postcard);

    void updateChatRoomLastMessage(Long chatRoomId, String lastChat, LocalDateTime lastChatTime);

    List<ChatRoomResponse> getChatRoomList(Long memberId);

    void updateIsAlert(Long roomId, Long memberId, Boolean isAlert);

    void updateChatRoomUnreadCount(Long memberId, Long chatRoomId, int unreadCount);


    void deleteMemberChatRoom(Long memberId, Long roomId);
}
