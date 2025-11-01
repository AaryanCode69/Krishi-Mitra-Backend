package com.example.krishimitrabackend.controllers;

import com.example.krishimitrabackend.dtos.CropSubmissionDTO;
import com.example.krishimitrabackend.services.UserRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/getRecords")
public class UserRecordController {
    private final UserRecordService userRecordService;


    @GetMapping("/allRecords")
    private ResponseEntity<List<CropSubmissionDTO>> getAllCropSubmission(){
        log.info("Getting all crop submissions");
        List<CropSubmissionDTO> responseList  = userRecordService.getAllCrops();
        return ResponseEntity.ok(responseList);
    }
}
