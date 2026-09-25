package com.PersonalExpense.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class LoginAttemptService {
    private final StringRedisTemplate stringRedisTemplate;

    public LoginAttemptService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public boolean isBlocked(String email){
        String attempts = stringRedisTemplate.opsForValue().get("login-attempts:" + email);
        return attempts != null && Long.parseLong(attempts) >= 5;
    }

    public void recordFailedAttempt(String email){
        Long attempts = stringRedisTemplate.opsForValue().increment("login-attempts:" + email);
        if (attempts != null && attempts == 1){
            stringRedisTemplate.expire("login-attempts:" + email, Duration.ofMinutes(15));
        }
    }

    public void resetAttempts(String email){
        stringRedisTemplate.delete("login-attempts:" + email);
    }
}
