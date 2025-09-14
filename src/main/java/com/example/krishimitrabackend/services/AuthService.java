package com.example.krishimitrabackend.services;

import com.example.krishimitrabackend.configs.JwtTokenProvider;
import com.example.krishimitrabackend.entities.UserEntity;
import com.example.krishimitrabackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

   private final JwtTokenProvider jwtTokenProvider;
   private final UserRepository userRepository;
   private final RedisTemplate<String, String> redisTemplate;
   private final SmsService smsService;

   public String sendOtp(String phoneNumber) {
       log.info("Sending OTP to "+phoneNumber);
       String otp = String.format("%06d",new Random().nextInt(999999));
       String redisKey = "otp:" + phoneNumber;
       redisTemplate.opsForValue().set(redisKey, otp,5, TimeUnit.MINUTES);
       smsService.sendSms(phoneNumber, otp);
       log.info("Sent OTP to "+phoneNumber);
       return "Message Sent Successfully";
   }

   @Transactional
   public String verifyOtp(String phoneNumber, String otp) {
       log.info("Verifying OTP to "+phoneNumber);
       String redisKey = "otp:" + phoneNumber;
       String storedOtp = redisTemplate.opsForValue().get(redisKey);
       if(storedOtp==null){
           throw new RuntimeException("Otp Expired or Invalid");
       }
       else if(!otp.equals(storedOtp)){
           throw new RuntimeException("Invalid OTP");
       }
       redisTemplate.delete(redisKey);
       UserEntity user = userRepository.findByPhoneNumber(phoneNumber);
       if(user==null){
           user = new UserEntity();
           user.setPhoneNumber(phoneNumber);
           user = userRepository.save(user);
       }
       String jwt = jwtTokenProvider.generateToken(user);
       log.info("JWT Generated Token: "+jwt);
       return jwt;
   }
}
