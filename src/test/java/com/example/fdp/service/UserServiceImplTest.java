package com.example.fdp.service;

import com.example.fdp.dto.request.CreateUserRequest;
import com.example.fdp.dto.request.UpdateUserRoleRequest;
import com.example.fdp.dto.request.UpdateUserStatusRequest;
import com.example.fdp.dto.response.UserResponse;
import com.example.fdp.exceptions.custom.UserNotFoundException;
import com.example.fdp.mapper.UserMapper;
import com.example.fdp.model.Role;
import com.example.fdp.model.User;
import com.example.fdp.model.UserStatus;
import com.example.fdp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private User user;
    private UserResponse userResponse;
    private CreateUserRequest createUserRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@gmail.com");
        user.setPassword("hashedPassword");
        user.setRole(Role.VIEWER);
        user.setStatus(UserStatus.ACTIVE);

        userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setName("Test User");
        userResponse.setEmail("test@gmail.com");
        userResponse.setRole(Role.VIEWER);
        userResponse.setStatus(UserStatus.ACTIVE);

        createUserRequest = new CreateUserRequest();
        createUserRequest.setName("Test User");
        createUserRequest.setEmail("test@gmail.com");
        createUserRequest.setPassword("password123");
        createUserRequest.setRole(Role.VIEWER);
    }

    @Test
    void createUser_Success() {
        when(userRepository.existsByEmail("test@gmail.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse response = userServiceImpl.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals("Test User", response.getName());
        assertEquals("test@gmail.com", response.getEmail());
        assertEquals(Role.VIEWER, response.getRole());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_DuplicateEmail_ThrowsException() {
        when(userRepository.existsByEmail("test@gmail.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userServiceImpl.createUser(createUserRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getAllUsers_Success() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        List<UserResponse> responses = userServiceImpl.getAllUsers();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("Test User", responses.get(0).getName());
    }

    @Test
    void getUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse response = userServiceImpl.getUserById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    @Test
    void getUserById_NotFound_ThrowsException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userServiceImpl.getUserById(99L));
    }

    @Test
    void updateUserRole_Success() {
        UpdateUserRoleRequest request = new UpdateUserRoleRequest();
        request.setRole(Role.ANALYST);

        userResponse.setRole(Role.ANALYST);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse response = userServiceImpl.updateUserRole(1L, request);

        assertNotNull(response);
        assertEquals(Role.ANALYST, response.getRole());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUserRole_UserNotFound_ThrowsException() {
        UpdateUserRoleRequest request = new UpdateUserRoleRequest();
        request.setRole(Role.ANALYST);

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userServiceImpl.updateUserRole(99L, request));
    }

    @Test
    void updateUserStatus_Success() {
        UpdateUserStatusRequest request = new UpdateUserStatusRequest();
        request.setStatus(UserStatus.INACTIVE);

        userResponse.setStatus(UserStatus.INACTIVE);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse response = userServiceImpl.updateUserStatus(1L, request);

        assertNotNull(response);
        assertEquals(UserStatus.INACTIVE, response.getStatus());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUserStatus_UserNotFound_ThrowsException() {
        UpdateUserStatusRequest request = new UpdateUserStatusRequest();
        request.setStatus(UserStatus.INACTIVE);

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userServiceImpl.updateUserStatus(99L, request));
    }
}