package com.example.wws.service;

import com.example.wws.dto.request.CompositionImportDTO;
import com.example.wws.entity.Composition;
import com.example.wws.entity.Student;
import com.example.wws.entity.User;
import com.example.wws.repository.CompositionRepository;
import com.example.wws.repository.StudentRepository;
import com.example.wws.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CompositionService {
    private final CompositionRepository compositionRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Value("${python.api.url}")
    private String pythonApiUrl;

    public CompositionService(CompositionRepository compositionRepository, StudentRepository studentRepository,
                              UserRepository userRepository, RestTemplate restTemplate) {
        this.compositionRepository = compositionRepository;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
    }

    public List<Composition> importCompositions(CompositionImportDTO request) {
        List<Composition> savedCompositions = new ArrayList<>();

        var chineseTeacher = userRepository.findById(request.getChineseTeacherId())
                .orElseThrow(() -> new RuntimeException("Chinese teacher not found"));

        for (var item : request.getCompositions()) {
            var student = studentRepository.findByStudentId(item.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student not found: " + item.getStudentId()));

            Composition composition = new Composition();
            composition.setStudent(student);
            composition.setChineseTeacher(chineseTeacher);
            composition.setContent(item.getContent());
            composition.setImagePath(item.getImagePath());
            composition.setSubmitTime(LocalDateTime.now());
            composition.setTransferredToPsychologist(false);

            // 调用 Python AI 获取分析结果
            Map<String, Object> result = analyzeComposition(item.getContent());

            // 将 AI 返回的四个维度分值存入数据库
            composition.setAcademicScore(Double.valueOf(result.getOrDefault("academic_score", 0.0).toString()));
            composition.setSocialScore(Double.valueOf(result.getOrDefault("social_score", 0.0).toString())); // 人际关系
            composition.setEmotionScore(Double.valueOf(result.getOrDefault("emotion_score", 0.0).toString()));
            composition.setSelfScore(Double.valueOf(result.getOrDefault("self_score", 0.0).toString()));

            composition.setHasPsychologicalIssue((Boolean) result.getOrDefault("has_issue", false));
            composition.setAnalysisResult((String) result.getOrDefault("result", "未生成分析报告"));

            savedCompositions.add(compositionRepository.save(composition));
        }

        return savedCompositions;
    }

    // 对接 Python 服务
    @SuppressWarnings("unchecked")
    private Map<String, Object> analyzeComposition(String content) {
        try {
            String baseUrl = pythonApiUrl;
            String fullUrl = baseUrl.endsWith("/") ? baseUrl + "analyze" : baseUrl + "/analyze";

            // 发送 POST 请求给 Python 端
            return restTemplate.postForObject(fullUrl, Map.of("content", content), Map.class);

        } catch (Exception e) {
            e.printStackTrace();
            // 如果 AI 服务挂了，返回默认值，防止 Java 程序崩溃
            return Map.of(
                    "has_issue", false,
                    "result", "AI服务连接失败",
                    "academic_score", 0.0,
                    "social_score", 0.0,
                    "emotion_score", 0.0,
                    "self_score", 0.0
            );
        }
    }

    // --- 以下是基础查询逻辑 ---

    public List<Composition> searchCompositions(Long schoolId, String studentId, String studentName, Boolean hasIssue) {
        List<Composition> allCompositions = compositionRepository.findByStudentSchoolId(schoolId);

        return allCompositions.stream()
                .filter(c -> studentId == null || c.getStudent().getStudentId().contains(studentId))
                .filter(c -> studentName == null || c.getStudent().getName().contains(studentName))
                .filter(c -> hasIssue == null || hasIssue.equals(c.getHasPsychologicalIssue()))
                .toList();
    }

    public Composition getCompositionById(Long id) {
        return compositionRepository.findById(id).orElseThrow(() -> new RuntimeException("Composition not found"));
    }

    public List<Composition> getCompositionsByChineseTeacher(Long teacherId) {
        return compositionRepository.findByChineseTeacherId(teacherId);
    }

    public List<Composition> getCompositionsWithPsychologicalIssue(Long schoolId) {
        var compositions = compositionRepository.findByStudentSchoolId(schoolId);
        return compositions.stream()
                .filter(c -> Boolean.TRUE.equals(c.getHasPsychologicalIssue()))
                .toList();
    }

    public Composition transferToPsychologist(Long compositionId) {
        Composition composition = getCompositionById(compositionId);
        composition.setTransferredToPsychologist(true);
        return compositionRepository.save(composition);
    }
}
