package com.example.krishimitrabackend.controllers;

import com.example.krishimitrabackend.dtos.CropSubmissionResponseDTO;
import com.example.krishimitrabackend.dtos.MLServiceResponseDTO;
import com.example.krishimitrabackend.services.MLResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/analysis")
@RequiredArgsConstructor
public class MLServiceController {

    private final MLResponseService mlResponseService;

    @PostMapping("/result")
    public ResponseEntity<String> uploadResult(@RequestBody MLServiceResponseDTO mlServiceResponseDTO) {
        String response = mlResponseService.saveMlServiceResponse(mlServiceResponseDTO);
        return ResponseEntity.ok(response);
    }
}
