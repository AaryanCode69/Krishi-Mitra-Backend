package com.example.krishimitrabackend.services;

import com.example.krishimitrabackend.dtos.UserRegisterationDTO;
import com.example.krishimitrabackend.entities.UserEntity;
import com.example.krishimitrabackend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserEntity getUserById(UUID id) throws EntityNotFoundException {
        return userRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("User with id : " + id.toString() + "not found"));
    }

    public UserRegisterationDTO createUser(UserRegisterationDTO user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication==null || !(authentication.getPrincipal() instanceof UserEntity)) {
            throw new IllegalStateException("Authentication required");
        }
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        UserEntity usertoUpdate = getUserById(currentUser.getId());
        modelMapper.map(user, usertoUpdate);
        usertoUpdate.setProfileComplete(true);
        usertoUpdate = userRepository.save(usertoUpdate);

        return modelMapper.map(usertoUpdate, UserRegisterationDTO.class);
    }
}
