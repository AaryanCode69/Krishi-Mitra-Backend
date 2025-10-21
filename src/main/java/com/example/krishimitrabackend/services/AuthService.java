package com.example.krishimitrabackend.services;

import com.example.krishimitrabackend.entities.UserEntity;
import com.example.krishimitrabackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

   private final UserRepository userRepository;
   private final RedisTemplate<String, String> redisTemplate;
   private final SmsService smsService;
   private final JwtService jwtService;
   private static final String OTP_PREFIX = "otp:";
   private static final SecureRandom random = new SecureRandom();

   @Value("${app.otp.expiry-minutes}")
   private long otpExpirationMinutes;

   public void sendOtp(String phoneNumber) {
       log.info("Sending OTP to "+phoneNumber);
       String otp = String.valueOf(100000 + random.nextInt(900000));
       String redisKey = OTP_PREFIX + phoneNumber;
       try{
           redisTemplate.opsForValue().set(redisKey, otp, otpExpirationMinutes, TimeUnit.MINUTES);
           smsService.sendSms(phoneNumber, otp);
           log.info("OTP sent to "+phoneNumber);
       }catch (Exception e){
           log.error("Error sending OTP to {} : {}  ",phoneNumber, e.getMessage());
           redisTemplate.delete(redisKey);
           throw new RuntimeException("Error sending OTP to " + phoneNumber);
       }
   }

   @Transactional
   public String verifyOtp(String phoneNumber, String otp) {
       log.info("Verifying OTP to "+phoneNumber);
       String redisKey = OTP_PREFIX + phoneNumber;
       String storedOtp = redisTemplate.opsForValue().getAndDelete(redisKey);
       log.info("OTP is "+storedOtp);
       if(storedOtp==null){
           log.warn("OTP verification failed for {}: OTP expired or does not exist", phoneNumber);
           throw new RuntimeException("Otp Expired or Invalid");
       }
       else if(!otp.equals(storedOtp)){
           log.warn("OTP verification failed for {}: Invalid OTP", phoneNumber);
           throw new RuntimeException("Invalid OTP");
       }
       UserEntity user = userRepository.findByPhoneNumber(phoneNumber);
       if(user==null){
           user = new UserEntity();
           user.setPhoneNumber(phoneNumber);
           user.setProfileComplete(false);
           userRepository.save(user);
       }
       log.info("OTP verified successfully for {}",phoneNumber);
       return jwtService.generateToken(user);
   }
}
