package com.example.fdp.service;

import com.example.fdp.dto.request.LoginRequest;
import com.example.fdp.dto.response.AuthResponse;
import com.example.fdp.exceptions.custom.UnauthorizedAccessException;
import com.example.fdp.model.Role;
import com.example.fdp.model.User;
import com.example.fdp.model.UserStatus;
import com.example.fdp.repository.UserRepository;
import com.example.fdp.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    private User activeUser;
    private User inactiveUser;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        activeUser = new User();
        activeUser.setId(1L);
        activeUser.setName("Admin User");
        activeUser.setEmail("admin@gmail.com");
        activeUser.setPassword("hashedPassword");
        activeUser.setRole(Role.ADMIN);
        activeUser.setStatus(UserStatus.ACTIVE);

        inactiveUser = new User();
        inactiveUser.setId(2L);
        inactiveUser.setName("Inactive User");
        inactiveUser.setEmail("inactive@gmail.com");
        inactiveUser.setPassword("hashedPassword");
        inactiveUser.setRole(Role.VIEWER);
        inactiveUser.setStatus(UserStatus.INACTIVE);

        loginRequest = new LoginRequest();
        loginRequest.setEmail("admin@gmail.com");
        loginRequest.setPassword("password123");
    }

    @Test
    void login_Success() {
        when(userRepository.findByEmail("admin@gmail.com")).thenReturn(Optional.of(activeUser));
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername("admin@gmail.com")).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails)).thenReturn("mockToken");

        AuthResponse response = authServiceImpl.login(loginRequest);

        assertNotNull(response);
        assertEquals("mockToken", response.getToken());
        assertEquals("Admin User", response.getName());
        assertEquals("admin@gmail.com", response.getEmail());
        assertEquals(Role.ADMIN, response.getRole());
    }

    @Test
    void login_InactiveUser_ThrowsException() {
        loginRequest.setEmail("inactive@gmail.com");
        when(userRepository.findByEmail("inactive@gmail.com")).thenReturn(Optional.of(inactiveUser));

        assertThrows(UnauthorizedAccessException.class, () -> authServiceImpl.login(loginRequest));
    }

    @Test
    void login_InvalidCredentials_ThrowsException() {
        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        assertThrows(BadCredentialsException.class, () -> authServiceImpl.login(loginRequest));
    }

    @Test
    void login_UserNotFound_ThrowsException() {
        when(userRepository.findByEmail("admin@gmail.com")).thenReturn(Optional.empty());

        assertThrows(UnauthorizedAccessException.class, () -> authServiceImpl.login(loginRequest));
    }
}