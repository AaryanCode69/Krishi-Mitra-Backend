package com.example.krishimitrabackend.repository;

import com.example.krishimitrabackend.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByPhoneNumber(String phoneNumber);

    Optional<UserEntity> findById(UUID id);
}
