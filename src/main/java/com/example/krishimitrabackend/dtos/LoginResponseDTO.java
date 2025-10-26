package com.example.krishimitrabackend.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class LoginResponseDTO {
    private UUID userId;
    private String accessToken;
    private String refreshToken;
}
