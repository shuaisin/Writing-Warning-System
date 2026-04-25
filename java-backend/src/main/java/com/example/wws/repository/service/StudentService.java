
package com.example.wws.service;

import com.example.wws.dto.request.StudentImportDTO;
import com.example.wws.entity.Student;
import com.example.wws.entity.User;
import com.example.wws.repository.SchoolRepository;
import com.example.wws.repository.StudentRepository;
import com.example.wws.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final SchoolRepository schoolRepository;
    private final UserRepository userRepository;
    
    public StudentService(StudentRepository studentRepository, SchoolRepository schoolRepository, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.schoolRepository = schoolRepository;
        this.userRepository = userRepository;
    }
    
    public List<Student> importStudents(StudentImportDTO request) {
        List<Student> savedStudents = new ArrayList<>();
        
        var school = schoolRepository.findById(request.getSchoolId())
                .orElseThrow(() -> new RuntimeException("School not found"));
        
        User headTeacher = null;
        if (request.getHeadTeacherId() != null) {
            headTeacher = userRepository.findById(request.getHeadTeacherId())
                    .orElseThrow(() -> new RuntimeException("Head teacher not found"));
        }
        
        for (var item : request.getStudents()) {
            if (!studentRepository.existsByStudentId(item.getStudentId())) {
                Student student = new Student();
                student.setStudentId(item.getStudentId());
                student.setName(item.getName());
                student.setClassName(item.getClassName());
                student.setGrade(item.getGrade());
                student.setSchool(school);
                student.setHeadTeacher(headTeacher);
                savedStudents.add(studentRepository.save(student));
            }
        }
        
        return savedStudents;
    }
    
    public List<Student> getStudentsBySchool(Long schoolId) {
        return studentRepository.findBySchoolId(schoolId);
    }
    
    public List<Student> getStudentsByHeadTeacher(Long headTeacherId) {
        return studentRepository.findByHeadTeacherId(headTeacherId);
    }
    
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Student not found"));
    }
    
    public Student getStudentByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
    }
}
