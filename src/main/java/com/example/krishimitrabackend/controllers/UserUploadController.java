package com.example.krishimitrabackend.controllers;

import com.example.krishimitrabackend.dtos.PresignedUrlDTO;
import com.example.krishimitrabackend.services.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/upload")
@Slf4j
public class UserUploadController {

    private final S3Service s3Service;

    @GetMapping("/presignedurl")
    public ResponseEntity<PresignedUrlDTO> presginedUrl(@RequestParam String fileName) {
        log.info("Request received for user presgined url");
        PresignedUrlDTO presignedUrlDTO = s3Service.generatePresignedUrl(fileName);
        log.info("PresignedUrlDTO generated for user presgined url");
        return ResponseEntity.ok(presignedUrlDTO);
    }
}
