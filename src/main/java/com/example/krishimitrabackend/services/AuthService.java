package com.example.krishimitrabackend.services;

import com.example.krishimitrabackend.configs.JwtTokenProvider;
import com.example.krishimitrabackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

   private final JwtTokenProvider jwtTokenProvider;
   private final UserRepository userRepository;
   private final RedisTemplate<String, String> redisTemplate;


}
