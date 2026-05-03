package com.example.wws.controller;

import com.example.wws.dto.request.UserCreateDTO;
import com.example.wws.dto.response.ApiResponse;
import com.example.wws.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @PostMapping
    public ApiResponse<?> createUser(@RequestBody UserCreateDTO request) {
        try {
            var user = userService.createUser(request);
            return ApiResponse.success(user);
        } catch (Exception e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }
    
    @GetMapping("/school/{schoolId}")
    public ApiResponse<?> getUsersBySchool(@PathVariable Long schoolId) {
        return ApiResponse.success(userService.getUsersBySchool(schoolId));
    }
    
    @GetMapping("/school/{schoolId}/role/{role}")
    public ApiResponse<?> getUsersBySchoolAndRole(@PathVariable Long schoolId, @PathVariable String role) {
        return ApiResponse.success(userService.getUsersBySchoolAndRole(schoolId, role));
    }
}
