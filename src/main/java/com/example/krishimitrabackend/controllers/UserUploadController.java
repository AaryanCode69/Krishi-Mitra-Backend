package com.example.krishimitrabackend.controllers;

import com.example.krishimitrabackend.dtos.CropSubmissionResponseDTO;
import com.example.krishimitrabackend.dtos.PresignedUrlDTO;
import com.example.krishimitrabackend.dtos.RabbitMQDTO;
import com.example.krishimitrabackend.services.CropSubmissionService;
import com.example.krishimitrabackend.services.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/upload")
@Slf4j
public class UserUploadController {

    private final S3Service s3Service;
    private final CropSubmissionService cropSubmissionService;

    @GetMapping("/presignedurl")
    public ResponseEntity<PresignedUrlDTO> presginedUrl(@RequestParam String fileName) {
        log.info("Request received for user presgined url");
        PresignedUrlDTO presignedUrlDTO = s3Service.generatePresignedUrl(fileName);
        log.info("PresignedUrlDTO generated for user presgined url");
        return ResponseEntity.ok(presignedUrlDTO);
    }

    @PostMapping("/confirmUpload")
    public ResponseEntity<RabbitMQDTO> confirmUpload(@RequestParam String objectKey) {
        log.info("Request received for user confirmed upload");
        RabbitMQDTO response = cropSubmissionService.cropSubmissionAndNotify(objectKey);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/proccessed/{submissionId}")
    public ResponseEntity<CropSubmissionResponseDTO> proccessed(@PathVariable UUID submissionId) {
        log.info("Request received for user proccessed upload");
        CropSubmissionResponseDTO cropSubmissionResponseDTO = cropSubmissionService.getProcessedResponse(submissionId);
        return ResponseEntity.ok(cropSubmissionResponseDTO);
    }


}
