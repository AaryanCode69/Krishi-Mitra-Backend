package com.example.krishimitrabackend.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class MLServiceResponseDTO {
    private UUID submissionId;
    private String diseaseName;
    private Double confidence;
    private String remedy;
}
