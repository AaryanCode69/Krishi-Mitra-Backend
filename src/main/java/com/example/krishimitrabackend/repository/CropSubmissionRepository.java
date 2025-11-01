package com.example.krishimitrabackend.repository;

import com.example.krishimitrabackend.entities.CropSubmission;
import com.example.krishimitrabackend.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CropSubmissionRepository extends JpaRepository<CropSubmission, UUID> {
    Optional<CropSubmission> findById(UUID submissionId);

    UserEntity user(UserEntity user);

    @Query("SELECT cs FROM CropSubmission cs LEFT JOIN FETCH cs.diseases WHERE cs.user = :user")
    List<CropSubmission> findByUserWithDiseases(@Param("user") UserEntity currentUser);
}
