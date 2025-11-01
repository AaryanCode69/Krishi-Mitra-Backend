package com.example.krishimitrabackend.services;

import com.example.krishimitrabackend.dtos.CropSubmissionDTO;
import com.example.krishimitrabackend.entities.CropSubmission;
import com.example.krishimitrabackend.entities.Diseases;
import com.example.krishimitrabackend.entities.UserEntity;
import com.example.krishimitrabackend.entities.enums.Status;
import com.example.krishimitrabackend.repository.CropSubmissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRecordService {
    private final CropSubmissionRepository cropSubmissionRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public List<CropSubmissionDTO> getAllCrops() {
        log.info("Checking User Authentication");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationException("User is not Authenticated"){};
        }
        log.info("Checking Crop Submission Repository");
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        List<CropSubmission> allCropSubmission = cropSubmissionRepository.findByUserWithDiseases(currentUser);
        log.info("Returning the Results");
        return allCropSubmission.stream().map(
                this::mapEntityToSummaryDTO).collect(Collectors.toList());
    }

    private CropSubmissionDTO mapEntityToSummaryDTO(CropSubmission submission) {

        CropSubmissionDTO dto = modelMapper.map(submission, CropSubmissionDTO.class);


        if (submission.getStatus() == Status.Completed) {
            List<Diseases> diseasesList = submission.getDiseases();
            if (diseasesList == null || diseasesList.isEmpty()) {
                dto.setDiseaseName("Healthy");
            } else {
                dto.setDiseaseName(diseasesList.get(0).getScientificName());
            }
        } else if (submission.getStatus() == Status.Processing) {
            dto.setDiseaseName("Analysis in Progress...");
        } else {
            dto.setDiseaseName("Analysis Failed");
        }
        dto.setId(submission.getId());
        dto.setImageUrl(submission.getImage_url());

        return dto;
    }
}
