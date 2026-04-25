package com.example.wws.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompositionSearchDTO {
    private Long schoolId;
    private String studentId;
    private String studentName;
    private Boolean hasPsychologicalIssue;
}
