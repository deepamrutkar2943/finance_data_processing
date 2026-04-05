package com.example.fdp.service;

import com.example.fdp.dto.request.LoginRequest;
import com.example.fdp.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
}