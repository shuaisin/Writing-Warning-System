package com.wws.dto;

import lombok.Data;
import java.util.Map;

/**
 * AiResultDTO: 用于接收 Python ai_service.py 返回的结构化数据
 * 字段需与 Python 返回的 JSON Key 保持一致
 */
@Data
public class AiResultDTO {
    // 学生唯一标识
    private String studentId;     
    
    // 经过 OCR 识别或直接输入的作文文本内容
    private String content;       
    
    // 四维心理分值映射：academic, social, emotion, self
    private Map<String, Double> scores; 
    
    // AI 生成的专家诊断简报
    private String expertReport;  
    
    // 是否触发风险预警 (情绪分 > 0.7)
    private Boolean isWarning;    
}
