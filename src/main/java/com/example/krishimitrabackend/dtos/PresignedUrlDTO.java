package com.example.krishimitrabackend.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PresignedUrlDTO {
    private String url;
    private String objectKey;
}
