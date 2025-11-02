package com.example.krishimitrabackend.services;

import com.example.krishimitrabackend.dtos.DataGovRecordDTO;
import com.example.krishimitrabackend.dtos.DataGovResponseDTO;
import com.example.krishimitrabackend.dtos.MandiResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MandiService {

    @Value("${spring.mandi.prices.api.key}")
    private String apiKey;

    private final RedisTemplate<String, Object> redisTemplate;
    private final static String prefix = "mandi:";
    private final RestTemplate restTemplate;

    public List<MandiResponseDTO> getMandiPrices(String state) {

        log.info("Checking if Commodity Available in Redis");
        String redisKey = prefix + state;
        List<MandiResponseDTO> response = (List<MandiResponseDTO>) redisTemplate.opsForValue().get(redisKey);
        if(response == null || response.isEmpty()){
            log.info("Commodity Not Avaliable in Redis Fetching From Api Key");
            String baseUrl = "https://api.data.gov.in/resource/35985678-0d79-46b4-9ed6-6f13308a1d24";

            String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .queryParam("api-key", apiKey)
                    .queryParam("format", "json")
                    .queryParam("filters[State]", state)
                    .queryParam("limit", 10)
                    .toUriString();
            try{
                DataGovResponseDTO responseDTO = restTemplate.getForObject(url, DataGovResponseDTO.class);
                if(responseDTO == null || responseDTO.getRecords() ==null || responseDTO.getRecords().isEmpty()){
                    log.info("No Commodity Available for this State");
                    return Collections.emptyList();
                }
                response = responseDTO.getRecords().stream().map(this::transformToMandiPriceDTO).collect(Collectors.toList());
                redisTemplate.opsForValue().set(redisKey, response,1, TimeUnit.HOURS);
            }catch (Exception e){
                log.error("Couldnt Fetch the State Details");
                throw new RuntimeException("Couldnt Fetch the State Details");
            }
        }
        return response;
    }

    private MandiResponseDTO transformToMandiPriceDTO(DataGovRecordDTO record) {
        MandiResponseDTO dto = new MandiResponseDTO();
        dto.setState(record.getState());
        dto.setDistrict(record.getDistrict());
        dto.setMarket(record.getMarket());
        dto.setCommodity(record.getCommodity());
        dto.setArrivalDate(record.getArrivalDate());
        dto.setMinPrice(safeParseInt(record.getMinPrice()));
        dto.setMaxPrice(safeParseInt(record.getMaxPrice()));
        dto.setModalPrice(safeParseInt(record.getModalPrice()));

        return dto;
    }

    private Integer safeParseInt(String value) {
        if (value == null) return null;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.warn("Could not parse price value: {}", value);
            return null;
        }
    }
}
