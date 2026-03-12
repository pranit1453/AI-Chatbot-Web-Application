package com.demo.ai.service;

import com.demo.ai.dto.request.CreateUserRequest;
import com.demo.ai.dto.request.UpdateUserRequest;
import com.demo.ai.dto.response.UserResponse;
import com.demo.ai.exception.custom.DatabaseOperationException;
import com.demo.ai.exception.custom.DuplicateDataException;
import com.demo.ai.exception.custom.InvalidDataException;
import com.demo.ai.exception.custom.ResourceNotFoundException;
import com.demo.ai.mapper.UserMapper;
import com.demo.ai.model.entities.User;
import com.demo.ai.model.enums.Role;
import com.demo.ai.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    // ================= CREATE USER =================

    @Transactional
    @Override
    public UserResponse createUser(CreateUserRequest request) {

        validateRequest(request);

        String username = normalize(request.username());
        String email = normalizeEmail(request.email());

        log.info("Creating user with username {}", username);

        validateDuplicateUser(username, email, null);

        try {

            User user = userMapper.toEntity(request);

            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(request.password()));
            user.setRole(Role.USER);

            User saved = userJpaRepository.save(user);

            log.info("User created successfully with id {}", saved.getUserId());

            return userMapper.toResponse(saved);

        } catch (DataIntegrityViolationException ex) {

            log.error("Database error while creating user {}", username, ex);

            throw new DatabaseOperationException("Failed to create user");
        }
    }

    // ================= GET USER =================

    @Override
    public UserResponse getUserById(UUID userId) {

        validateUserId(userId);

        log.info("Fetching user {}", userId);

        User user = findUser(userId);

        return userMapper.toResponse(user);
    }

    // ================= UPDATE USER =================

    @Transactional
    @Override
    public UserResponse updateUser(UUID userId, UpdateUserRequest request) {

        validateUserId(userId);
        validateRequest(request);

        log.info("Updating user {}", userId);

        User user = findUser(userId);

        String username = normalize(request.username());
        String email = normalizeEmail(request.email());

        validateDuplicateUser(username, email, user);

        try {

            userMapper.updateUser(request, user);

            if (username != null) user.setUsername(username);
            if (email != null) user.setEmail(email);

            User saved = userJpaRepository.save(user);

            log.info("User updated successfully {}", userId);

            return userMapper.toResponse(saved);

        } catch (DataIntegrityViolationException ex) {

            log.error("Database error while updating user {}", userId, ex);

            throw new DatabaseOperationException("Failed to update user");
        }
    }

    // ================= SOFT DELETE =================

    @Transactional
    @Override
    public void deleteUser(UUID userId) {

        validateUserId(userId);

        log.info("Soft deleting user {}", userId);

        User user = findUser(userId);

        try {

            userJpaRepository.delete(user);

            log.info("User soft deleted {}", userId);

        } catch (DataIntegrityViolationException ex) {

            log.error("Database error while deleting user {}", userId, ex);

            throw new DatabaseOperationException("Failed to delete user");
        }
    }

    // ================= RESTORE USER =================

    @Transactional
    @Override
    public UserResponse restoreUser(UUID userId) {

        validateUserId(userId);

        log.info("Restoring user {}", userId);

        User user = userJpaRepository.findByUserIdAndDeletedTrue(userId).orElseThrow(() -> new ResourceNotFoundException("Deleted user not found"));

        try {

            user.setDeleted(false);
            user.setDeletedAt(null);

            User saved = userJpaRepository.save(user);

            log.info("User restored {}", userId);

            return userMapper.toResponse(saved);

        } catch (DataIntegrityViolationException ex) {

            log.error("Database error while restoring user {}", userId, ex);

            throw new DatabaseOperationException("Failed to restore user");
        }
    }

    // ================= HARD DELETE =================

    @Transactional
    @Override
    public void permanentlyDelete(UUID userId) {

        validateUserId(userId);

        log.info("Permanently deleting user {}", userId);

        User user = userJpaRepository.findByUserIdAndDeletedTrue(userId).orElseThrow(() -> new ResourceNotFoundException("Deleted user not found"));

        try {

            userJpaRepository.delete(user);

            log.info("User permanently deleted {}", userId);

        } catch (DataIntegrityViolationException ex) {

            log.error("Database error while permanently deleting user {}", userId, ex);

            throw new DatabaseOperationException("Failed to permanently delete user");
        }
    }

    // ================= UPDATE LAST LOGIN =================

    @Transactional
    @Override
    public void updateLastLogin(UUID userId) {

        validateUserId(userId);

        log.info("Updating last login for user {}", userId);

        User user = findUser(userId);

        try {

            user.setLastLoginAt(Instant.now());

            userJpaRepository.save(user);

            log.info("Last login updated for user {}", userId);

        } catch (DataIntegrityViolationException ex) {

            log.error("Database error while updating last login {}", userId, ex);

            throw new DatabaseOperationException("Failed to update last login");
        }
    }

    // ================= HELPER METHODS =================

    private void validateUserId(UUID userId) {
        if (userId == null) {
            throw new InvalidDataException("User ID cannot be null");
        }
    }

    private void validateRequest(Object request) {
        if (request == null) {
            throw new InvalidDataException("Request cannot be null");
        }
    }

    private User findUser(UUID userId) {
        return userJpaRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private String normalize(String value) {
        return value == null ? null : value.trim();
    }

    private String normalizeEmail(String value) {
        return value == null ? null : value.trim().toLowerCase();
    }

    private void validateDuplicateUser(String username, String email, User existingUser) {

        if (username != null && (existingUser == null || !username.equals(existingUser.getUsername())) && userJpaRepository.existsByUsername(username)) {

            throw new DuplicateDataException("Username already exists");
        }

        if (email != null && (existingUser == null || !email.equals(existingUser.getEmail())) && userJpaRepository.existsByEmail(email)) {

            throw new DuplicateDataException("Email already exists");
        }
    }
}