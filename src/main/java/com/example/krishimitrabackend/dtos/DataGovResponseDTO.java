package com.example.krishimitrabackend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataGovResponseDTO {
    private List<DataGovRecordDTO> records;
}
