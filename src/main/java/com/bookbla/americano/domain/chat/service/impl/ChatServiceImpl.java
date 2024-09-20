package com.bookbla.americano.domain.chat.service.impl;

import com.bookbla.americano.domain.chat.controller.dto.*;
import com.bookbla.americano.domain.chat.enums.SendChatStatus;
import com.bookbla.americano.domain.chat.repository.ChatRepository;
import com.bookbla.americano.domain.chat.repository.MemberChatRoomRepository;
import com.bookbla.americano.domain.chat.repository.entity.Chat;
import com.bookbla.americano.domain.chat.repository.entity.ChatRoom;

import com.bookbla.americano.domain.chat.repository.entity.MemberChatRoom;
import com.bookbla.americano.domain.chat.service.ChatRoomService;
import com.bookbla.americano.domain.chat.service.ChatService;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.notification.service.AlarmService;
import com.bookbla.americano.domain.notification.service.InAppAlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final EntityManager em;

    private final MemberChatRoomRepository memberChatRoomRepository;

    private final AlarmService alarmService;

    private final InAppAlarmService inAppAlarmService;

    private final ChatRoomService chatRoomService;

    private final MemberRepository memberRepository;

    private final SimpUserRegistry userRegistry;


    private final SimpMessagingTemplate messagingTemplate;

    public Page<ChatDto> getChatList(Long roomId, Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by("sendTime").descending());
        Page<Chat> chats = chatRepository.findByChatRoom_Id(roomId, pageRequest);

        return chats.map(ChatDto::of);
    }

    public Chat save(ChatPubMessage chatPub) {
        Chat chat = Chat.builder()
                .sendTime(LocalDateTime.now())
                .content(chatPub.getContent())
                .sender(em.getReference(Member.class, chatPub.getSenderId()))
                .chatRoom(em.getReference(ChatRoom.class, chatPub.getChatRoomId()))
                .build();
        return chatRepository.save(chat);
    }

    @Override
    public void readChatAll(Long roomId, Long memberId) {
        chatRepository.updateIsReadByChatRoomId(roomId, memberId, true);
    }

    @Override
    @Transactional
    public void handleSendChat(ChatPubMessage chatDto) {

        // 채팅방에 참여하고 있는 Member
        List<Member> members;

        // 채팅방에 참여하고 있는 Member 중 sender 를 제외한 Member(상대방)
        List<Member> otherMembers;

        // 채팅 정보
        Chat result;
        try {
            Chat.ChatBuilder chatBuilder = Chat.builder()
                    .sendTime(LocalDateTime.now())
                    .content(chatDto.getContent())
                    .sender(em.getReference(Member.class, chatDto.getSenderId()))
                    .chatRoom(em.getReference(ChatRoom.class, chatDto.getChatRoomId()));

            // 채팅방에 참여하고 있는 Member 불러오기
            // MemberChatRoom FetchJoin
            members = memberRepository.findByChatroomId(chatDto.getChatRoomId());

            // 채팅방에 참여하고 있는 Member 중 sender 를 제외한 Member(상대방) 불러오기
            otherMembers = members.stream().filter(member -> !member.getId().equals(chatDto.getSenderId()))
                    .collect(Collectors.toList());

            // 채팅방에 상대방이 없으면 아무것도 하지 않음
            if (otherMembers.isEmpty()) {
                throw new Exception();
            }

            // 해당 Member 들이 채팅방에 접속했는지 확인
            Set<SimpSubscription> otherSubscription = userRegistry.findSubscriptions(subscription -> {
                try {
                    String destination = subscription.getDestination();
                    String[] split = destination.split("/");
                    String subPath = split[split.length -3];
                    Long roomId = Long.valueOf(split[split.length - 2]);
                    Long userId = Long.valueOf(split[split.length - 1]);
                    for (Member member : otherMembers) {
                        if (subPath.equals("room") && roomId.equals(chatDto.getChatRoomId())
                                && userId.equals(member.getId())) {
                            return true;
                        }
                    }
                    return false;
                } catch (Exception e) {
                    return false;
                }

            });

            // 상대방이 채팅방에 접속해있으면
            if (!otherSubscription.isEmpty()) {
                chatBuilder.isRead(true);
            } else {
                chatBuilder.isRead(false);
                // 상대방이 채팅방에 접속해있지 않으면 unreadCount += 1
                MemberChatRoom memberchatRoom = memberChatRoomRepository.findByMember_IdAndChatRoom_Id(otherMembers.get(0).getId(), chatDto.getChatRoomId())
                        .orElseThrow();
                memberchatRoom.setUnreadCount(memberchatRoom.getUnreadCount() + 1);
                memberChatRoomRepository.save(memberchatRoom);
            }

            // 채팅 저장
            result = chatRepository.save(chatBuilder.build());


            // 채팅방의 마지막 채팅 정보 업데이트
            chatRoomService.updateChatRoomLastMessage(result.getChatRoom().getId(), result.getContent(), result.getSendTime());
        } catch (Exception e) {
            log.error(" failed to handle chat : " + e);
            ChatSubMessage subMessage = ChatSubMessage.from(chatDto);
            subMessage.setStatus(SendChatStatus.FAIL);
            subMessage.setRead(false);

            messagingTemplate.convertAndSend("/topic/chat/"+chatDto.getSenderId(), subMessage);

            return;
        }

        // 채팅방에 포함된 Member 들의 Websocket 에 채팅 정보 전송(Sender 포함)
        ChatSubMessage subMessage = ChatSubMessage.from(result);
        Set<SimpSubscription> subscriptions = userRegistry.findSubscriptions(subscription -> {
            String destination = subscription.getDestination();
            String[] split = destination.split("/");
            for (Member member : members) {
                if (split[split.length - 2].equals("chat") &&
                        split[split.length - 1].equals(member.getId().toString())) {
                    return true;
                }
            }
            return false;
        });
        subMessage.setStatus(SendChatStatus.SUCCESS);
        subscriptions.forEach(simpSubscription -> {
            messagingTemplate.convertAndSend(simpSubscription.getDestination(), subMessage);
        });


        // ChatRoom 에 속한 Member 들에게 알람 전송
        for (Member member : otherMembers) {
            // InAppAlarm 전송 실패 시 PushAlarm 전송
            try {
                inAppAlarmService.sendChatInAppAlarm(result, member.getId());
            } catch (Exception e) {
                alarmService.sendPushAlarmForChat(otherMembers, result);
            }
        }

    }

    @Override
    public void handleMemberRoomConnect(Long roomId, Long memberId, RoomConnection connection) {
        // member 의 상대방 채팅을 모두 읽음 처리함
        readChatAll(roomId, memberId);
        chatRoomService.updateChatRoomUnreadCount(memberId, roomId, 0);

        // 상대방에게 member 의 Connection Status 를 알림
        List<Member> otherMembers = memberRepository.findByChatroomId(roomId)
                .stream().filter(member -> !member.getId().equals(memberId))
                .collect(Collectors.toList());
        for (Member member : otherMembers) {
            messagingTemplate.convertAndSend("/topic/chat/room/" + roomId + "/" + member.getId(), connection);
        }

    }
}
