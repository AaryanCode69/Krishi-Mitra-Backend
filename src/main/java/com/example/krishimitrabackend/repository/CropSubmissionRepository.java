package com.example.krishimitrabackend.repository;

import com.example.krishimitrabackend.entities.CropSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CropSubmissionRepository extends JpaRepository<CropSubmission, Long> {
    Optional<CropSubmission> findById(UUID submissionId);
}
