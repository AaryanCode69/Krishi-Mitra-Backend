package com.example.krishimitrabackend.controllers;

import com.example.krishimitrabackend.dtos.UserRegisterationDTO;
import com.example.krishimitrabackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/signUp")
public class UserRegistrationController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserRegisterationDTO> registerUser(@RequestBody UserRegisterationDTO userRegisterationDTO) {
        return new ResponseEntity<>(userService.createUser(userRegisterationDTO), HttpStatus.CREATED);
    }

}
