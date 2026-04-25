
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
            
            Map<String, Object> result = analyzeComposition(item.getContent());
            composition.setHasPsychologicalIssue((Boolean) result.get("has_issue"));
            composition.setAnalysisResult((String) result.get("result"));
            
            savedCompositions.add(compositionRepository.save(composition));
        }
        
        return savedCompositions;
    }
    
    @SuppressWarnings("unchecked")
    private Map<String, Object> analyzeComposition(String content) {
        try {
            return restTemplate.postForObject(pythonApiUrl, Map.of("content", content), Map.class);
        } catch (Exception e) {
            return Map.of("has_issue", false, "result", "Analysis service unavailable");
        }
    }
    
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
