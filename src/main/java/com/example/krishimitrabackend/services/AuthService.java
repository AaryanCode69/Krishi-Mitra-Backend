package com.example.krishimitrabackend.services;

import com.example.krishimitrabackend.dtos.LoginResponseDTO;
import com.example.krishimitrabackend.entities.UserEntity;
import com.example.krishimitrabackend.exception.RateLimitException;
import com.example.krishimitrabackend.repository.UserRepository;
import com.twilio.exception.ApiException;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

   private final RateLimitingService rateLimitingService;
   private final UserRepository userRepository;
   private final RedisTemplate<String, Object> redisTemplate;
   private final SmsService smsService;
   private final JwtService jwtService;
   private static final String OTP_PREFIX = "otp:";
   private static final SecureRandom random = new SecureRandom();

   @Value("${app.otp.expiry-minutes}")
   private long otpExpirationMinutes;

   public String sendOtp(String phoneNumber) {
       log.info("Attempting to Sending OTP to "+phoneNumber);
       Bucket otpBucket = rateLimitingService.bucketForOTp(phoneNumber);
       ConsumptionProbe probe = otpBucket.tryConsumeAndReturnRemaining(1);

       if(!probe.isConsumed()) {
           long waitForRefillSeconds = probe.getNanosToWaitForRefill() / 1_000_000_000;
           log.warn("Ratelimit exceeded for {} please wait for {} seconds", phoneNumber, waitForRefillSeconds+1);
           throw new RateLimitException("Ratelimit exceeded for please wait for " + (waitForRefillSeconds+1) + " seconds");
       }
       log.info("Requested is not RateLimited :- Sending OTP to "+phoneNumber);
       String otp = String.valueOf(100000 + random.nextInt(900000));
       String redisKey = OTP_PREFIX + phoneNumber;
       try{
           redisTemplate.opsForValue().set(redisKey, otp, otpExpirationMinutes, TimeUnit.MINUTES);
           smsService.sendSms(phoneNumber, otp);
           log.info("OTP sent to "+phoneNumber);
           return "Message sent Successfully";
       }catch (ApiException e) {
           log.error("Twilio API Error sending OTP to {}: Code={} Message={}", phoneNumber, e.getCode(), e.getMessage());
           redisTemplate.delete(redisKey);
           throw e;
       }
       catch (Exception e){
           log.error("Error sending OTP to {} : {}  ",phoneNumber, e.getMessage());
           redisTemplate.delete(redisKey);
           throw new RuntimeException("Error sending OTP to " + phoneNumber);
       }
   }

   @Transactional
   public LoginResponseDTO verifyOtp(String phoneNumber, String otp) {
       log.info("Verifying OTP to "+phoneNumber);
       String redisKey = OTP_PREFIX + phoneNumber;
       String storedOtp = (String) redisTemplate.opsForValue().getAndDelete(redisKey);
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
           user = userRepository.save(user);
       }
       log.info("OTP verified successfully for {}",phoneNumber);
       LoginResponseDTO loginResponseDTO = LoginResponseDTO.builder()
               .userId(user.getId())
               .accessToken(jwtService.generateAccessToken(user))
               .refreshToken(jwtService.generateRefreshToken(user))
               .build();
       return loginResponseDTO;
   }

   public LoginResponseDTO refreshToken(String refreshToken) {
       UUID userId = jwtService.getUserIdFromToken(refreshToken);
       UserEntity user = userRepository.findById(userId).orElse((null));
       if(user==null){
           throw new EntityNotFoundException("User not found ,not a valid token");
       }
       String accessToken = jwtService.generateAccessToken(user);
       return  LoginResponseDTO.builder().userId(user.getId()).refreshToken(refreshToken).accessToken(accessToken).build();
   }
}
