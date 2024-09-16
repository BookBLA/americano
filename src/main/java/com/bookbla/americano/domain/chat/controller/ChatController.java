package com.bookbla.americano.domain.chat.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.chat.controller.dto.ChatDto;
import com.bookbla.americano.domain.chat.controller.dto.ChatPubMessage;
import com.bookbla.americano.domain.chat.controller.dto.RoomConnection;
import com.bookbla.americano.domain.chat.service.ChatRoomService;
import com.bookbla.americano.domain.chat.service.ChatService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    @MessageMapping("/chat/{userId}")
    public void sendMessage(@DestinationVariable Long userId, Message<ChatPubMessage> message) {
        ChatPubMessage chat = message.getPayload();
        chatService.handleSendChat(chat);
    }

    @MessageMapping("/chat/room/{roomId}/{memberId}")
    public void sendRoomConnectionMessage(@DestinationVariable Long roomId, @DestinationVariable Long memberId,
                                                    Message<RoomConnection> message) {
        chatService.handleMemberRoomConnect(roomId, memberId, message.getPayload());
    }

    @GetMapping("/chat")
    public ResponseEntity<Page<ChatDto>> getChatList(
            @Parameter(hidden = true) @User LoginUser loginUser,
            @RequestParam Long roomId,
            Pageable pageable) {
       Page<ChatDto> result = chatService.getChatList(roomId, pageable);
       return ResponseEntity.ok(result);
    }
}
