package com.wws.client;

import com.wws.dto.AiResultDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * AiAnalysisClient: 负责与 Python AI 中台通信的核心组件
 */
@Component
public class AiAnalysisClient {

    // Python 服务的地址，建议后续放入 application.yml 配置文件中
    private final String AI_SERVICE_URL = "http://localhost:8000/analyze";

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 调用 AI 接口进行作文分析
     * @param studentId 学生ID
     * @param text 作文内容 (如果是图片，建议先由 Java 调用 OCR 或直接传文本)
     * @return 封装好的分析结果对象
     */
    public AiResultDTO analyzeEssay(String studentId, String text) {
        // 1. 设置请求头，对应 Python FastAPI 的 Form 表单接收模式
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 2. 构造表单参数
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("student_id", studentId);
        requestBody.add("text", text);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            // 3. 发起同步 POST 请求并自动解析 JSON 为 DTO
            ResponseEntity<AiResultDTO> response = restTemplate.postForEntity(
                AI_SERVICE_URL, 
                requestEntity, 
                AiResultDTO.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
        } catch (Exception e) {
            // 这里的日志打印可以根据你的审美进行优化，比如“画外之音”检测到信号中断
            System.err.println("【WWS】AI 服务连接失败: " + e.getMessage());
        }
        
        return null; // 或者返回一个带有默认中位分值的空对象
    }
}
