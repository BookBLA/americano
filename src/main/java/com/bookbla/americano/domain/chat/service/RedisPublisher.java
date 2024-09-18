//package com.bookbla.americano.domain.chat.service;
//
//import com.bookbla.americano.domain.chat.controller.dto.ChatSubMessage;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.listener.ChannelTopic;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//
//@RequiredArgsConstructor
//@Component
//@Slf4j
//public class RedisPublisher {
//
//    private final ChannelTopic channelTopic;
//
//    private final RedisTemplate<byte[], byte[]> redisTemplate;
//
//    public void publish(ChatSubMessage message) {
//        log.info("RedisPublisher publishing .. {}", message.getContent());
//        redisTemplate.convertAndSend(channelTopic.getTopic(), message);
//    }
//}
