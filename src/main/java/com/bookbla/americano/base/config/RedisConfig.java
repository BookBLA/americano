package com.bookbla.americano.base.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "redis")
public class RedisConfig {

    private String host;
    private int port;

    @Bean // Redis와 연결
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean // RedisConnection을 통해 넘어온 byte값을 객체 직렬화
    public RedisTemplate<byte[], byte[]> redisTemplate() {
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }

//    /*
//       Chat Config
//     */
//    @Bean
//    public ChannelTopic channelTopic() {
//        return new ChannelTopic("chatroom");
//    }
//
//    // redis : chatroom topic 에 publish 된 메시지 처리를 위한 리스너 설정
//    @Bean
//    public RedisMessageListenerContainer redisMessageListener(MessageListenerAdapter listenerAdapter, ChannelTopic topic) {
//        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//        container.setConnectionFactory(redisConnectionFactory());
//        container.addMessageListener(listenerAdapter, topic);
//        return container;
//    }
//
//    // 메시지 처리 Subscriber
//    @Bean
//    public MessageListenerAdapter listenerAdapter(RedisSubscriber subscriber) {
//        return new MessageListenerAdapter(subscriber, "sendMessage");
//    }
}
