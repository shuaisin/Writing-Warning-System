package com.example.wws.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "student_profile")
@Data
public class StudentProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @Column(columnDefinition = "TEXT")
    private String psychologicalHistory;
    
    private Integer overallRiskLevel;
    
    private LocalDateTime lastUpdateTime;
}