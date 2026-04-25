package com.example.wws.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentImportDTO {
    private Long schoolId;
    private Long headTeacherId;
    private List<StudentItemDTO> students;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudentItemDTO {
        private String studentId;
        private String name;
        private String className;
        private Integer grade;
    }
}
