package com.example.krishimitrabackend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataGovRecordDTO {
    @JsonProperty("State")
    private String state;

    @JsonProperty("District")
    private String district;

    @JsonProperty("Market")
    private String market;

    @JsonProperty("Commodity")
    private String commodity;

    @JsonProperty("Arrival_Date")
    private String arrivalDate;

    @JsonProperty("Min_Price")
    private String minPrice;

    @JsonProperty("Max_Price")
    private String maxPrice;

    @JsonProperty("Modal_Price")
    private String modalPrice;
}
