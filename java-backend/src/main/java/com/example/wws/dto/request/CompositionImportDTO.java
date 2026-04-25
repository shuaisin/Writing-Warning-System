package com.example.wws.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompositionImportDTO {
    private Long chineseTeacherId;
    private List<CompositionItemDTO> compositions;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompositionItemDTO {
        private String studentId;
        private String content;
        private String imagePath;
    }
}
