package com.namo.spring.db.redis.util;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisUtil {

    private final RedisTemplate<String, String> redisTemplate;

    public void saveWithTTL(String key, String value, long ttlInSeconds) {
        try {
            redisTemplate.opsForValue().set(key, value, ttlInSeconds, TimeUnit.SECONDS);
            log.info("Redis에 저장 완료: key={}, value={}, TTL={}초", key, value, ttlInSeconds);
        } catch (Exception e) {
            log.error("Redis 저장 실패: key={}, value={}, 이유={}", key, value, e.getMessage());
        }
    }

    public String get(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("Redis에서 key={} 조회 실패: 이유={}", key, e.getMessage());
            return null;
        }
    }

    public List<String> getKeys(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        return keys != null ? keys.stream().toList() : List.of();
    }

    public void delete(String key) {
        try {
            redisTemplate.delete(key);
            log.info("Redis에서 key={} 삭제 완료", key);
        } catch (Exception e) {
            log.error("Redis에서 key={} 삭제 실패: 이유={}", key, e.getMessage());
        }
    }
    

}
