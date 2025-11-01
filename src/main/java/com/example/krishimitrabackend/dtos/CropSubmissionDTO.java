package com.example.krishimitrabackend.dtos;

import com.example.krishimitrabackend.entities.enums.Status;
import lombok.*;

import java.util.UUID;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CropSubmissionDTO {
    private UUID id;
    private String imageUrl;
    private Status status;
    private String diseaseName;
}
