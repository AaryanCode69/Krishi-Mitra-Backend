package com.example.krishimitrabackend.repository;

import com.example.krishimitrabackend.entities.CropSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CropSubmissionRepository extends JpaRepository<CropSubmission, Long> {
}
