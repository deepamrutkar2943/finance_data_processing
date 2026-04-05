package com.example.fdp.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fdp.dto.request.CreateUserRequest;
import com.example.fdp.dto.request.UpdateUserRoleRequest;
import com.example.fdp.dto.request.UpdateUserStatusRequest;
import com.example.fdp.dto.response.UserResponse;
import com.example.fdp.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "Admin only - manage users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}

	// ADMIN only - create a new user
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Create user", description = "Admin only - create a new user")
	public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
	}

	// ADMIN only - get all users
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Get all users", description = "Admin only - get list of all users")
	public ResponseEntity<List<UserResponse>> getAllUsers() {
		return ResponseEntity.ok(userService.getAllUsers());
	}

	// ADMIN only - get user by id
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Get user by id", description = "Admin only - get user by id")
	public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
		return ResponseEntity.ok(userService.getUserById(id));
	}

	// ADMIN only - update user role
	@PutMapping("/{id}/role")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Update user role", description = "Admin only - update role of a user")
	public ResponseEntity<UserResponse> updateUserRole(@PathVariable Long id,
			@Valid @RequestBody UpdateUserRoleRequest request) {
		return ResponseEntity.ok(userService.updateUserRole(id, request));
	}

	// ADMIN only - update user status
	@PutMapping("/{id}/status")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Update user status", description = "Admin only - activate or deactivate user")
	public ResponseEntity<UserResponse> updateUserStatus(@PathVariable Long id,
			@Valid @RequestBody UpdateUserStatusRequest request) {
		return ResponseEntity.ok(userService.updateUserStatus(id, request));
	}
}