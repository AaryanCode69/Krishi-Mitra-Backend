package com.example.krishimitrabackend.dtos;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserRegisterationDTO {

    private String fullName;
    private String city;
    private String state;
    private String district;

}
