package com.example.krishimitrabackend.controllers;

import com.example.krishimitrabackend.dtos.OtpVerificationDTO;
import com.example.krishimitrabackend.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/")
public class AuthController {


    private final AuthService authService;

    @PostMapping("/getOtp/{number}")
    public ResponseEntity<String> getOtp(@PathVariable("number") String number){
        authService.sendOtp(number);
        return ResponseEntity.ok("Otp Sent Successfully");
    }

    @PostMapping("/verifyOtp")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpVerificationDTO otpVerificationDTO){
        String message = authService.verifyOtp(otpVerificationDTO.getPhoneNumber(),  otpVerificationDTO.getOtp());
        return ResponseEntity.ok(message);
    }

}
