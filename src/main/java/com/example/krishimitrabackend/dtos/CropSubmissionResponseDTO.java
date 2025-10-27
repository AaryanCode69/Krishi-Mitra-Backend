package com.example.krishimitrabackend.dtos;

import com.example.krishimitrabackend.entities.enums.Status;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CropSubmissionResponseDTO {
    private UUID submissionId;
    private String diseaseName;
    private String remedy;
    private Status status;
    private Double confidenceScore;
    private String imageUrl;
}
