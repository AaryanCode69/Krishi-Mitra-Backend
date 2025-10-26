package com.example.krishimitrabackend.services;

import com.example.krishimitrabackend.dtos.CropSubmissionResponseDTO;
import com.example.krishimitrabackend.dtos.MLServiceResponseDTO;
import com.example.krishimitrabackend.entities.CropSubmission;
import com.example.krishimitrabackend.entities.Diseases;
import com.example.krishimitrabackend.entities.enums.Status;
import com.example.krishimitrabackend.repository.CropSubmissionRepository;
import com.example.krishimitrabackend.repository.DiseaseRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MLResponseService {

    private final DiseaseRepository diseaseRepository;

    private final CropSubmissionRepository  cropSubmissionRepository;

    @Transactional
    public String saveMlServiceResponse(MLServiceResponseDTO mlServiceResponseDTO){
        CropSubmission cropSubmission = cropSubmissionRepository
                .findById(mlServiceResponseDTO.getSubmissionId())
                .orElseThrow(()-> {
                    log.error("Crop Submission Not Found for Id{}", mlServiceResponseDTO.getSubmissionId().toString());
                            return new EntityNotFoundException("Crop Submission Not Found");
                        }
                );

        cropSubmission.setStatus(Status.Completed);
        cropSubmission.setConfidenceScore(mlServiceResponseDTO.getConfidence());

        Diseases diseases = diseaseRepository
                .findByScientificName(mlServiceResponseDTO.getDiseaseName())
                .orElseGet(()->{
                    log.info("Disease not found for name {} , add new entry",mlServiceResponseDTO.getDiseaseName());
                    Diseases newDisease = new Diseases();
                    newDisease.setScientificName(mlServiceResponseDTO.getDiseaseName());
                    newDisease.setRecommendedActions(mlServiceResponseDTO.getRemedy());
                    return diseaseRepository.save(newDisease);
                });
        List<Diseases> diseasesList = new ArrayList<>();
        diseasesList.add(diseases);
        cropSubmission.setDiseases(diseasesList);
        cropSubmissionRepository.save(cropSubmission);

        return "Processed Data is Saved Successfully";
    }

}
