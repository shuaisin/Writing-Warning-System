
package com.example.wws.repository;

import com.example.wws.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByStudentId(String studentId);
    List<Student> findBySchoolId(Long schoolId);
    List<Student> findByHeadTeacherId(Long headTeacherId);
    List<Student> findBySchoolIdAndClassName(Long schoolId, String className);
    List<Student> findBySchoolIdAndGrade(Long schoolId, Integer grade);
    boolean existsByStudentId(String studentId);
}
