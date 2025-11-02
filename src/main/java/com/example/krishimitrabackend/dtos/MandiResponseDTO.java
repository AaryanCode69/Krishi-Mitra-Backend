package com.example.krishimitrabackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MandiResponseDTO {
    private String state;
    private String district;
    private String market;
    private String commodity;
    private String arrivalDate;
    private Integer minPrice;
    private Integer maxPrice;
    private Integer modalPrice;
}
