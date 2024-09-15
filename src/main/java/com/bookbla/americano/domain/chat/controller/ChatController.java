package com.bookbla.americano.domain.chat.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.chat.controller.dto.ChatDto;
import com.bookbla.americano.domain.chat.repository.MemberChatRoomRepository;
import com.bookbla.americano.domain.chat.repository.entity.Chat;
import com.bookbla.americano.domain.chat.service.ChatRoomService;
import com.bookbla.americano.domain.chat.service.ChatService;
import com.bookbla.americano.domain.notification.service.AlarmService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpSubscriptionMatcher;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final AlarmService alarmService;
    private final ChatRoomService chatRoomService;

    private SimpUserRegistry simpUserRegistry;

    @MessageMapping("/chat/{userId}")
    @SendTo("/topic/chat/{userId}")
    public ChatDto sendMessage(@DestinationVariable Long userId, Message<ChatDto> message) {
        ChatDto chat = message.getPayload();

        //채팅 저장
        Chat result = chatService.save(chat);

        // receiver 에게 알람 전송
        alarmService.sendPushAlarmForChat(result);

        // 채팅방의 마지막 채팅 정보 업데이트
        chatRoomService.updateChatRoomLastMessage(result.getChatRoom().getId(), result.getContent(), result.getSendTime());

//        Set<SimpSubscription> subs = simpUserRegistry.findSubscriptions(subscription -> subscription.getDestination().equals("/topic/chat/"+userId));
//        if (!subs.isEmpty()) {
//
//        }

        return chat;
    }



    @GetMapping("/chat")
    public ResponseEntity<Page<ChatDto>> getChatList(
            @Parameter(hidden = true) @User LoginUser loginUser,
            @RequestParam Long roomId,
            Pageable pageable) {
       Page<ChatDto> result = chatService.getChatList(roomId, pageable);
       // 채팅방을 열면 해당 채팅방의 unread count 를 0으로 업데이트
       chatRoomService.updateChatRoomUnreadCount(loginUser.getMemberId(), roomId, 0);
       return ResponseEntity.ok(result);
    }
}
