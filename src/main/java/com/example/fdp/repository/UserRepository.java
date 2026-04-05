package com.example.fdp.repository;

import com.example.fdp.model.Role;
import com.example.fdp.model.User;
import com.example.fdp.model.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Used during login and checking duplicate email
    Optional<User> findByEmail(String email);

    // Used to check if email already exists before creating user
    boolean existsByEmail(String email);

    // Used by admin to filter users by role
    List<User> findByRole(Role role);

    // Used by admin to filter users by status
    List<User> findByStatus(UserStatus status);
}