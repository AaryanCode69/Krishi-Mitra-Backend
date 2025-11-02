package com.example.krishimitrabackend.controllers;

import com.example.krishimitrabackend.dtos.MandiResponseDTO;
import com.example.krishimitrabackend.services.MandiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class MandiController {

    private final MandiService mandiService;

    @GetMapping("/mandi")
    public ResponseEntity<List<MandiResponseDTO>> getMandiPrices(@RequestParam String State) {
        log.info("Getting mandi prices for state {}", State);
        List<MandiResponseDTO> response = mandiService.getMandiPrices(State);
        log.info("Successfully retrieved mandi prices for state {}", State);
        return ResponseEntity.ok(response);
    }
}
