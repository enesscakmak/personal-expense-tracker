package com.PersonalExpense.service;

import com.PersonalExpense.common.InvalidCredentialsException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private final StringRedisTemplate stringRedisTemplate;

    public RefreshTokenService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public String create(Long userId){
        String token = UUID.randomUUID().toString();
        stringRedisTemplate.opsForValue().set("refresh:" + token, userId.toString(), Duration.ofDays(7));
        stringRedisTemplate.opsForSet().add("user-refresh:" + userId, token);
        stringRedisTemplate.expire("user-refresh:" + userId, Duration.ofDays(7));
        return token;
    }

    public Long getUserIdAndDeleteToken(String token){
        String userId = stringRedisTemplate.opsForValue().getAndDelete("refresh:" + token);
        if (userId == null){
            throw new InvalidCredentialsException("Invalid or expired refresh token.");
        }
        return Long.valueOf(userId);
    }

    public void delete(String token){
        stringRedisTemplate.delete("refresh:" + token);
    }

    public void deleteAllForUser(Long userId){
        Set<String> tokens = stringRedisTemplate.opsForSet().members("user-refresh:" + userId);
        for (String token : tokens) {
            delete(token);
        }
        stringRedisTemplate.delete("user-refresh:" + userId);
    }
}
