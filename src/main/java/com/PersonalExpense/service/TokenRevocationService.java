package com.PersonalExpense.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class TokenRevocationService {
    private final StringRedisTemplate stringRedisTemplate;
    private final JwtService jwtService;

    public TokenRevocationService(StringRedisTemplate stringRedisTemplate, JwtService jwtService){
        this.stringRedisTemplate = stringRedisTemplate;
        this.jwtService = jwtService;
    }

    public void revoke(String token){
        Duration timeLeft = Duration.between(Instant.now(), jwtService.getExpirationDateFromToken(token).toInstant());
        stringRedisTemplate.opsForValue().set(token, "revoked", timeLeft);
    }

    public boolean isRevoked(String token){
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(token));
    }

}
