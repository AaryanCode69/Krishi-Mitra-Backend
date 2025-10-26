package com.example.krishimitrabackend.services;

import com.example.krishimitrabackend.configs.RabbitMqConfig;
import com.example.krishimitrabackend.dtos.CropSubmissionResponseDTO;
import com.example.krishimitrabackend.dtos.RabbitMQDTO;
import com.example.krishimitrabackend.entities.CropSubmission;
import com.example.krishimitrabackend.entities.UserEntity;
import com.example.krishimitrabackend.entities.enums.Status;
import com.example.krishimitrabackend.repository.CropSubmissionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class CropSubmissionService {

    @Value("${cloud.aws.bucket.name}")
    private String bucketName;

    @Value("${cloud.aws.region}")
    private String region;

    private final CropSubmissionRepository cropSubmissionRepository;

    private final ModelMapper modelMapper;

    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public RabbitMQDTO cropSubmissionAndNotify(String objectKey) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationException("User is not Authenticated"){};
        }
        String standardUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, objectKey);
        UserEntity user = (UserEntity) authentication.getPrincipal();

        CropSubmission newSubmission = CropSubmission.builder()
                .user(user)
                .image_url(standardUrl)
                .status(Status.Processing)
                .build();

        newSubmission =  cropSubmissionRepository.save(newSubmission);

        RabbitMQDTO rabbitMQDTO = RabbitMQDTO.builder()
                .submissionId(newSubmission.getId())
                .bucketName(bucketName)
                .objectKey(objectKey)
                .build();

        rabbitTemplate.convertAndSend(RabbitMqConfig.Exchange_Name,RabbitMqConfig.RoutingKey_Name,rabbitMQDTO);

        return rabbitMQDTO;
    }

    
    public CropSubmissionResponseDTO getProcessedResponse(UUID submissionId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationException("User is not Authenticated"){};
        }

        CropSubmission cropSubmission = cropSubmissionRepository
                .findById(submissionId)
                .orElseThrow(()-> new EntityNotFoundException("Crop Submission Not Found"));
        CropSubmissionResponseDTO cropSubmissionResponseDTO = modelMapper.map(cropSubmission, CropSubmissionResponseDTO.class);
        if(cropSubmission.getDiseases()==null || cropSubmission.getDiseases().isEmpty()) {
            cropSubmissionResponseDTO.setDiseaseName(null);
            cropSubmissionResponseDTO.setRemedy(null);
        }else{
         cropSubmissionResponseDTO.setRemedy(cropSubmission.getDiseases().get(0).getRecommendedActions());
         cropSubmissionResponseDTO.setDiseaseName(cropSubmission.getDiseases().get(0).getScientificName());
        }
        return cropSubmissionResponseDTO;
    }
}
