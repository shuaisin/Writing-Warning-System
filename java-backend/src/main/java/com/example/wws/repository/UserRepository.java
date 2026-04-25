
package com.example.wws.repository;

import com.example.wws.entity.Role;
import com.example.wws.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findBySchoolId(Long schoolId);
    List<User> findBySchoolIdAndRole(Long schoolId, Role role);
    boolean existsByUsername(String username);
}
