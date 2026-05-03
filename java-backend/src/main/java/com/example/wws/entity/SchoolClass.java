package com.example.wws.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "school_class")
@Data
public class SchoolClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    private Integer grade;
    
    @ManyToOne
    @JoinColumn(name = "school_id", nullable = false)
    private School school;
    
    @ManyToOne
    @JoinColumn(name = "head_teacher_id")
    private User headTeacher;
    
    @ManyToOne
    @JoinColumn(name = "psychology_teacher_id")
    private User psychologyTeacher;
}