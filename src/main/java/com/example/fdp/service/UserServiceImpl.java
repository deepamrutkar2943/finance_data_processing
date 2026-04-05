package com.example.fdp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.fdp.dto.request.CreateUserRequest;
import com.example.fdp.dto.request.UpdateUserRoleRequest;
import com.example.fdp.dto.request.UpdateUserStatusRequest;
import com.example.fdp.dto.response.UserResponse;
import com.example.fdp.exceptions.custom.UserNotFoundException;
import com.example.fdp.mapper.UserMapper;
import com.example.fdp.model.User;
import com.example.fdp.repository.UserRepository;

@Service

public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserMapper userMapper;

	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.userMapper = userMapper;
	}

	@Override
	public UserResponse createUser(CreateUserRequest request) {

		// Check duplicate email
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new IllegalArgumentException("Email already in use: " + request.getEmail());
		}

		User user = new User();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(request.getRole());

		User saved = userRepository.save(user);
		return userMapper.toResponse(saved);
	}

	@Override
	public List<UserResponse> getAllUsers() {
		return userRepository.findAll().stream().map(userMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public UserResponse getUserById(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
		return userMapper.toResponse(user);
	}

	@Override
	public UserResponse updateUserRole(Long id, UpdateUserRoleRequest request) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

		user.setRole(request.getRole());
		User updated = userRepository.save(user);
		return userMapper.toResponse(updated);
	}

	@Override
	public UserResponse updateUserStatus(Long id, UpdateUserStatusRequest request) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

		user.setStatus(request.getStatus());
		User updated = userRepository.save(user);
		return userMapper.toResponse(updated);
	}
}