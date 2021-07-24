package com.game.b1ingservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void setWithExp(String key, Object value, long exp) {
        redisTemplate.opsForValue().set(key, value, exp, TimeUnit.SECONDS);
    }

    public Boolean setEX(String key, Object value, long exp) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, exp, TimeUnit.SECONDS);
    }

    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

}
