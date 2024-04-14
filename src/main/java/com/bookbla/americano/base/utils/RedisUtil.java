package com.bookbla.americano.base.utils;


import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisUtil {
    private final StringRedisTemplate redisTemplate;

    public String getData(String key) {
        // key를 통해 value(데이터)를 얻음
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public void setDataExpire(String key, String value, long durationOfSeconds) {
        //  duration 동안 (key, value)를 저장

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(durationOfSeconds);
        valueOperations.set(key, value, expireDuration);
    }

    public void deleteData(String key) {
        // 데이터 삭제
        redisTemplate.delete(key);
    }
}
