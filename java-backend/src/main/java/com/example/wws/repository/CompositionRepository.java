
package com.example.wws.repository;

import com.example.wws.entity.Composition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompositionRepository extends JpaRepository<Composition, Long> {
    List<Composition> findByStudentId(Long studentId);
    List<Composition> findByChineseTeacherId(Long chineseTeacherId);
    List<Composition> findByStudentSchoolId(Long schoolId);
    List<Composition> findByHasPsychologicalIssue(Boolean hasIssue);
    List<Composition> findByHasPsychologicalIssueAndTransferredToPsychologist(Boolean hasIssue, Boolean transferred);
}
