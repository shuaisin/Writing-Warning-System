package com.example.wws.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "intervention_record")
@Data
public class InterventionRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @ManyToOne
    @JoinColumn(name = "analysis_id")
    private CompositionAnalysis analysis;
    
    private String sourceInitiator;
    
    @ManyToOne
    @JoinColumn(name = "current_handler_id")
    private User currentHandler;
    
    @Enumerated(EnumType.STRING)
    private InterventionStatus status;
    
    @Column(columnDefinition = "TEXT")
    private String feedbackNotes;
    
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}