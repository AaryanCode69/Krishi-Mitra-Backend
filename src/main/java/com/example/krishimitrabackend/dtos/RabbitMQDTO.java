package com.example.krishimitrabackend.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class RabbitMQDTO {
    private UUID submissionId;
    private String bucketName;
    private String objectKey;
}
