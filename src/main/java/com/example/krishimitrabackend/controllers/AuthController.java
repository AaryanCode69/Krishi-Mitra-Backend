package com.example.krishimitrabackend.controllers;

import com.example.krishimitrabackend.dtos.LoginResponseDTO;
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

    @GetMapping("/getOtp/{number}")
    public ResponseEntity<String> getOtp(@PathVariable("number") String number){
        String message = authService.sendOtp(number);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/verifyOtp")
    public ResponseEntity<LoginResponseDTO> verifyOtp(@RequestBody OtpVerificationDTO otpVerificationDTO){
        LoginResponseDTO loginResponseDTO = authService.verifyOtp(otpVerificationDTO.getPhoneNumber(),  otpVerificationDTO.getOtp());
        return ResponseEntity.ok(loginResponseDTO);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refreshAccessToken(@RequestParam String refreshToken){
        LoginResponseDTO loginResponseDTO = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(loginResponseDTO);
    }

}
