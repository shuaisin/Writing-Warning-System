package com.example.wws.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "composition_analysis")
@Data
public class CompositionAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "composition_id", nullable = false)
    private Composition composition;
    
    @Column(columnDefinition = "TEXT")
    private String analysisResult;
    
    private Double academicScore;
    private Double socialScore;
    private Double emotionScore;
    private Double selfScore;
    
    @Enumerated(EnumType.STRING)
    private RiskLevel aiAssessedLevel;
    
    @Column(nullable = false)
    private LocalDateTime analysisTime;
}