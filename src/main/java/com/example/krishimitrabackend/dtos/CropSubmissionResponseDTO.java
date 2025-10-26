package com.example.krishimitrabackend.dtos;

import com.example.krishimitrabackend.entities.enums.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class CropSubmissionResponseDTO {
    private UUID submissionId;
    private String diseaseName;
    private String remedy;
    private Status status;
    private Double confidenceScore;
    private String imageUrl;
}
