package com.example.krishimitrabackend.repository;

import com.example.krishimitrabackend.entities.Diseases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiseaseRepository extends JpaRepository<Diseases, Long> {
    Optional<Diseases> findByScientificName(String diseaseName);
}
