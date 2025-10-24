package com.example.krishimitrabackend.services;

import com.example.krishimitrabackend.dtos.PresignedUrlDTO;
import com.example.krishimitrabackend.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Presigner s3Presigner;

    @Value("${cloud.aws.bucket.name}")
    private String bucketName;

    public PresignedUrlDTO generatePresignedUrl(String originalFileName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null ||  !authentication.isAuthenticated()) {
            throw new AuthenticationException("Authentication failed , User is not Authenticated") {
            };
        }
        UserEntity user = (UserEntity) authentication.getPrincipal();
        UUID userId = user.getId();
        String fileName = userId.toString()+"-"+originalFileName;
        String objectKey = "user-uploads/"+userId.toString()+"/"+fileName;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15))
                .putObjectRequest(putObjectRequest)
                .build();
        PresignedPutObjectRequest presignedPutObjectRequest = s3Presigner.presignPutObject(putObjectPresignRequest);

        PresignedUrlDTO  presignedUrlDTO = new PresignedUrlDTO();
        presignedUrlDTO.setObjectKey(objectKey);
        presignedUrlDTO.setUrl(presignedPutObjectRequest.url().toString());
        return presignedUrlDTO;
    }
}
