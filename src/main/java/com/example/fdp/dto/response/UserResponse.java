package com.example.fdp.dto.response;

import java.time.LocalDateTime;

import com.example.fdp.model.Role;
import com.example.fdp.model.UserStatus;

public class UserResponse {

	private Long id;
	private String name;
	private String email;
	private Role role;
	private UserStatus status;
	private LocalDateTime createdAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public UserResponse(Long id, String name, String email, Role role, UserStatus status, LocalDateTime createdAt) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.role = role;
		this.status = status;
		this.createdAt = createdAt;
	}

	public UserResponse() {
		super();
	}

}