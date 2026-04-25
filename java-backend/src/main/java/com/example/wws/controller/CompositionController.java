
package com.example.wws.controller;

import com.example.wws.dto.request.CompositionImportDTO;
import com.example.wws.dto.response.ApiResponse;
import com.example.wws.service.CompositionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/compositions")
public class CompositionController {
    private final CompositionService compositionService;
    
    public CompositionController(CompositionService compositionService) {
        this.compositionService = compositionService;
    }
    
    @PostMapping("/import")
    public ApiResponse<?> importCompositions(@RequestBody CompositionImportDTO request) {
        try {
            var compositions = compositionService.importCompositions(request);
            return ApiResponse.success("Imported " + compositions.size() + " compositions", compositions);
        } catch (Exception e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }
    
    @GetMapping("/search")
    public ApiResponse<?> searchCompositions(
            @RequestParam Long schoolId,
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) String studentName,
            @RequestParam(required = false) Boolean hasPsychologicalIssue) {
        return ApiResponse.success(compositionService.searchCompositions(schoolId, studentId, studentName, hasPsychologicalIssue));
    }
    
    @GetMapping("/{id}")
    public ApiResponse<?> getComposition(@PathVariable Long id) {
        try {
            return ApiResponse.success(compositionService.getCompositionById(id));
        } catch (Exception e) {
            return ApiResponse.error(404, e.getMessage());
        }
    }
    
    @GetMapping("/teacher/{teacherId}")
    public ApiResponse<?> getCompositionsByTeacher(@PathVariable Long teacherId) {
        return ApiResponse.success(compositionService.getCompositionsByChineseTeacher(teacherId));
    }
    
    @GetMapping("/school/{schoolId}/issues")
    public ApiResponse<?> getCompositionsWithIssues(@PathVariable Long schoolId) {
        return ApiResponse.success(compositionService.getCompositionsWithPsychologicalIssue(schoolId));
    }
    
    @PutMapping("/{id}/transfer")
    public ApiResponse<?> transferToPsychologist(@PathVariable Long id) {
        try {
            return ApiResponse.success(compositionService.transferToPsychologist(id));
        } catch (Exception e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }
}
