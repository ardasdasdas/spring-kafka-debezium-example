package com.example.kafkadebeziumexample.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String CACHE_KEY_PREFIX = "product:";

    public void saveToCache(Long productId, String name, Double price) {
        String cacheKey = CACHE_KEY_PREFIX + productId;
        String value = name + ":" + price;
        redisTemplate.opsForValue().set(cacheKey, value);
    }

    public void removeFromCache(Long productId) {
        String cacheKey = CACHE_KEY_PREFIX + productId;
        redisTemplate.delete(cacheKey);
    }
}
