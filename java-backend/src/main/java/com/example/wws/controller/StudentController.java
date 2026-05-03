package com.example.wws.controller;

import com.example.wws.dto.request.StudentImportDTO;
import com.example.wws.dto.response.ApiResponse;
import com.example.wws.service.StudentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService studentService;
    
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }
    
    @PostMapping("/import")
    public ApiResponse<?> importStudents(@RequestBody StudentImportDTO request) {
        try {
            var students = studentService.importStudents(request);
            return ApiResponse.success("Imported " + students.size() + " students", students);
        } catch (Exception e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }
    
    @GetMapping("/school/{schoolId}")
    public ApiResponse<?> getStudentsBySchool(@PathVariable Long schoolId) {
        return ApiResponse.success(studentService.getStudentsBySchool(schoolId));
    }
    
    @GetMapping("/head-teacher/{headTeacherId}")
    public ApiResponse<?> getStudentsByHeadTeacher(@PathVariable Long headTeacherId) {
        return ApiResponse.success(studentService.getStudentsByHeadTeacher(headTeacherId));
    }
}
