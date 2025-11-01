package com.example.krishimitrabackend.dtos;

import com.example.krishimitrabackend.entities.enums.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
@Builder
public class CropSubmissionDTO {
    private UUID id;
    private String imageUrl;
    private Status status;
    private String diseaseName;
    private String remedy;
}
