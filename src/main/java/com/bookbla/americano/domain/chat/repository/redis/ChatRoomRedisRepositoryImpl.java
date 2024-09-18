//package com.bookbla.americano.domain.chat.repository.redis;
//
//import com.bookbla.americano.domain.chat.controller.dto.ChatRoomResponse;
//import com.bookbla.americano.domain.chat.repository.ChatRoomRedisRepository;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.redis.core.HashOperations;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Repository;
//
//import javax.annotation.Resource;
//import java.nio.ByteBuffer;
//
//@Repository
//@RequiredArgsConstructor
//@Slf4j
//public class ChatRoomRedisRepositoryImpl implements ChatRoomRedisRepository {
//
//    private final RedisTemplate<String, Object> redisChatTemplate;
//
//    private final ObjectMapper objectMapper;
//
//
//    @Resource(name = "redisTemplate")
//    private HashOperations<String, String, ChatRoomResponse> opsHashChatRoom;
//
//
//    public void setChatRoom(Long userId, Long roomId, ChatRoomResponse response) {
//        opsHashChatRoom.put(getChatRoomKey(userId), getRoomFieldName(roomId), response);
//    }
//
//    public boolean existChatRoom(Long userId, Long roomId) {
//        return opsHashChatRoom.hasKey(getChatRoomKey(userId), getRoomFieldName(roomId));
//    }
//
//
//    private String getChatRoomKey(Long userId) {
//        return userId.toString();
//    }
//
//    private String getRoomFieldName(Long roomId) {
//        return roomId.toString();
//    }
//
//
//}
