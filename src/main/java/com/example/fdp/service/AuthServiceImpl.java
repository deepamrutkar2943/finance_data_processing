package com.example.fdp.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.example.fdp.dto.request.LoginRequest;
import com.example.fdp.dto.response.AuthResponse;
import com.example.fdp.exceptions.custom.UnauthorizedAccessException;
import com.example.fdp.model.User;
import com.example.fdp.model.UserStatus;
import com.example.fdp.repository.UserRepository;
import com.example.fdp.security.JwtUtil;

@Service

public class AuthServiceImpl implements AuthService {

	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;
	private final UserDetailsService userDetailsService;

	public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, JwtUtil jwtUtil,
			UserDetailsService userDetailsService) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}

	@Override
	public AuthResponse login(LoginRequest request) {

		
		// Authenticate credentials
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

		// Load user from DB
		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new UnauthorizedAccessException("Invalid credentials"));

		// Block inactive users from logging in
		if (user.getStatus() == UserStatus.INACTIVE) {
			throw new UnauthorizedAccessException("Your account is inactive. Contact admin.");
		}

		// Generate token
		UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
		String token = jwtUtil.generateToken(userDetails);

		AuthResponse response = new AuthResponse();
		response.setToken(token);
		response.setName(user.getName());
		response.setEmail(user.getEmail());
		response.setRole(user.getRole());
		return response;
	}
}