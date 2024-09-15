package com.bookbla.americano.domain.chat.service.impl;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.base.exception.BaseExceptionType;
import com.bookbla.americano.domain.chat.controller.dto.ChatRoomResponse;
import com.bookbla.americano.domain.chat.repository.ChatRoomRepository;
import com.bookbla.americano.domain.chat.repository.MemberChatRoomRepository;
import com.bookbla.americano.domain.chat.repository.entity.ChatRoom;
import com.bookbla.americano.domain.chat.repository.entity.MemberChatRoom;
import com.bookbla.americano.domain.chat.service.ChatRoomService;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.postcard.repository.entity.Postcard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberChatRoomRepository memberChatRoomRepository;


    @Transactional
    public ChatRoom createChatRoom(List<Member> members, Postcard postcard) {
        // create new Chat Room
        ChatRoom newChatRoom = ChatRoom.builder()
                .postcard(postcard)
                .isAlert(true)
                .build();
        chatRoomRepository.save(newChatRoom);

        // create mapping tables for each members
        for (Member member : members) {
            memberChatRoomRepository.save(
                    MemberChatRoom.builder()
                            .chatRoom(newChatRoom)
                            .member(member)
                            .build()
            );
        }
        return newChatRoom;
    }

    public void deleteChatRoom(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow();
        chatRoomRepository.delete(chatRoom);
    }

    public List<ChatRoomResponse> getChatRoomList(Long memberId) {
        return chatRoomRepository.findByMemberId(memberId);
    }

    public void updateIsAlert(Long roomId, Boolean isAlert) {
        if (isAlert == null) {
            throw new BaseException(BaseExceptionType.ARGUMENT_NOT_VALID);
        }
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(
                () -> new BaseException(BaseExceptionType.ARGUMENT_NOT_VALID)
        );

        chatRoomRepository.updateIsAlert(roomId, isAlert);
    }

    @Override
    public void deleteMemberChatRoom(Long memberId, Long roomId) {
        MemberChatRoom mapper = memberChatRoomRepository.findByMember_IdAndChatRoom_Id(memberId, roomId)
                .orElseThrow(() -> new BaseException(BaseExceptionType.ARGUMENT_NOT_VALID));

        memberChatRoomRepository.delete(mapper);
    }

//    @Transactional(readOnly = true)
//    public List<ChatRoom> listChatRoom(Long memberId) {
//
//    }

}
