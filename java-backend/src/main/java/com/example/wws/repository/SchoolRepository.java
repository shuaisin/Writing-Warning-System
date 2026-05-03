package com.example.wws.repository;

import com.example.wws.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SchoolRepository extends JpaRepository<School, Long> {
    Optional<School> findByName(String name);
    boolean existsByName(String name);
}
