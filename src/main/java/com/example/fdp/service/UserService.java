package com.example.fdp.service;

import com.example.fdp.dto.request.CreateUserRequest;
import com.example.fdp.dto.request.UpdateUserRoleRequest;
import com.example.fdp.dto.request.UpdateUserStatusRequest;
import com.example.fdp.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(CreateUserRequest request);
    List<UserResponse> getAllUsers();
    UserResponse getUserById(Long id);
    UserResponse updateUserRole(Long id, UpdateUserRoleRequest request);
    UserResponse updateUserStatus(Long id, UpdateUserStatusRequest request);
}