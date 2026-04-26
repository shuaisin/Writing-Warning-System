
package com.example.wws.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "composition")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Composition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @ManyToOne
    @JoinColumn(name = "chinese_teacher_id", nullable = false)
    private User chineseTeacher;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    private String imagePath;
    
    @Column(nullable = false)
    private LocalDateTime submitTime;

    private Double academicScore; // 学业压力
    private Double socialScore;   // 人际关系
    private Double emotionScore;  // 情绪状态
    private Double selfScore;     // 自我认知
    
    private Boolean hasPsychologicalIssue;
    
    @Column(columnDefinition = "TEXT")
    private String analysisResult;
    
    private Boolean transferredToPsychologist = false;
}
