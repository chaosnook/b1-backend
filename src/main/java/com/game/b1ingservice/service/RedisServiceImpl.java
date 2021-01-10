package com.game.b1ingservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

//import static com.appworks.co.th.mastermanagement.commons.Constants.ACCESSKEY_PREFIX;
//import static com.appworks.co.th.mastermanagement.commons.Constants.VALID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    @Value("${mflow.accesskey.timout:900}")
    private long accesskeyTimout;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value, accesskeyTimout, TimeUnit.SECONDS);
    }
    
    @Override
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    @Override
    public Object get(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("Redis get : {}", e.getMessage());
            return null;
        }
    }

    @Override
    public boolean delete(String key) {
        try {
            return redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("Redis get : {}", e.getMessage());
            return false;
        }
    }


}
