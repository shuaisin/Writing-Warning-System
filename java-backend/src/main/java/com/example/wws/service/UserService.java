
package com.example.wws.service;

import com.example.wws.dto.request.UserCreateDTO;
import com.example.wws.entity.Role;
import com.example.wws.entity.User;
import com.example.wws.repository.SchoolRepository;
import com.example.wws.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserService(UserRepository userRepository, SchoolRepository schoolRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.schoolRepository = schoolRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public User createUser(UserCreateDTO request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.valueOf(request.getRole().toUpperCase()));
        user.setRealName(request.getRealName());
        user.setPhone(request.getPhone());
        
        if (request.getSchoolId() != null) {
            schoolRepository.findById(request.getSchoolId())
                    .ifPresent(user::setSchool);
        }
        
        return userRepository.save(user);
    }
    
    public List<User> getUsersBySchool(Long schoolId) {
        return userRepository.findBySchoolId(schoolId);
    }
    
    public List<User> getUsersBySchoolAndRole(Long schoolId, String role) {
        return userRepository.findBySchoolIdAndRole(schoolId, Role.valueOf(role.toUpperCase()));
    }
    
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
