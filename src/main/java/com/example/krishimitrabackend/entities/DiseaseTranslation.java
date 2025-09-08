package com.example.krishimitrabackend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class DiseaseTranslation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "disease_id")
    private Diseases diseases;

    @Column(unique = true, nullable = false)
    private String commonName;

    private String description;

    private String recommendedActions;

}
