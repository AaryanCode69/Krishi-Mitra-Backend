package com.example.krishimitrabackend.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpVerificationDTO {
    private String phoneNumber;
    private String otp;
}
