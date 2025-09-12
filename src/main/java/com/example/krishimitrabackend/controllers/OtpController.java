package com.example.krishimitrabackend.controllers;

import com.example.krishimitrabackend.services.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/otp")
public class OtpController {


    private final SmsService smsService;


}
