package com.bookbla.americano.domain.chat.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.chat.controller.dto.ChatRoomResponse;
import com.bookbla.americano.domain.chat.service.ChatRoomService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chat/room")
@RequiredArgsConstructor
public class ChatRoomController {


    private final ChatRoomService chatRoomService;

    @GetMapping
    public ResponseEntity<List<ChatRoomResponse>> getChatRoomList(@Parameter(hidden=true) @User LoginUser loginUser) {
        List<ChatRoomResponse> result = chatRoomService.getChatRoomList(loginUser.getMemberId());
        return ResponseEntity.ok(result.stream().peek(res -> res.getOtherMember().setSmokeType(res.getOtherMember().getSmokeTypeEnum().getDetailValue()))
                .collect(Collectors.toList()));
    }

    @GetMapping("/postcard")
    public ResponseEntity<ChatRoomResponse> getChatRoomByPostcardId(@Parameter(hidden=true) @User LoginUser loginUser, @RequestParam Long postcardId) {
        return ResponseEntity.ok(chatRoomService.getChatRoomByPostcardId(loginUser.getMemberId(), postcardId));
    }

    @PostMapping("/alert")
    public ResponseEntity<?> setIsAlert(@Parameter(hidden=true) @User LoginUser loginUser,
                                        @RequestParam Boolean isAlert,
                                        @RequestParam Long roomId) {
        chatRoomService.updateIsAlert(roomId, loginUser.getMemberId(), isAlert);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/exit")
    public ResponseEntity<?> exitChatRoom(@Parameter(hidden = true) @User LoginUser loginUser,
                                          @RequestParam Long roomId) {

        chatRoomService.deleteMemberChatRoom(loginUser.getMemberId(), roomId);
        return ResponseEntity.ok(null);
    }
}
