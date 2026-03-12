package com.demo.ai.controller;

import com.demo.ai.dto.request.CreateUserRequest;
import com.demo.ai.dto.request.UpdateUserRequest;
import com.demo.ai.dto.response.UserResponse;
import com.demo.ai.service.UserService;
import com.demo.ai.wrapper.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ================= CREATE USER =================

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody CreateUserRequest request) {

        log.info("API request to create user");

        UserResponse response = userService.createUser(request);

        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .message("User account successfully created")
                .data(response)
                .failureMessage(null)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    // ================= GET USER =================

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(
            @PathVariable UUID userId) {

        log.info("API request to fetch user {}", userId);

        UserResponse response = userService.getUserById(userId);

        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("User fetched successfully")
                .data(response)
                .failureMessage(null)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // ================= UPDATE USER =================

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable UUID userId,
            @Valid @RequestBody UpdateUserRequest request) {

        log.info("API request to update user {}", userId);

        UserResponse response = userService.updateUser(userId, request);

        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("User updated successfully")
                .data(response)
                .failureMessage(null)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // ================= SOFT DELETE =================

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable UUID userId) {

        log.info("API request to soft delete user {}", userId);

        userService.deleteUser(userId);

        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("User deleted successfully")
                .data(null)
                .failureMessage(null)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // ================= RESTORE USER =================

    @PatchMapping("/{userId}/restore")
    public ResponseEntity<ApiResponse<UserResponse>> restoreUser(
            @PathVariable UUID userId) {

        log.info("API request to restore user {}", userId);

        UserResponse response = userService.restoreUser(userId);

        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("User restored successfully")
                .data(response)
                .failureMessage(null)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // ================= PERMANENT DELETE =================

    @DeleteMapping("/{userId}/permanent")
    public ResponseEntity<ApiResponse<Void>> permanentlyDelete(
            @PathVariable UUID userId) {

        log.info("API request to permanently delete user {}", userId);

        userService.permanentlyDelete(userId);

        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("User permanently deleted")
                .data(null)
                .failureMessage(null)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // ================= UPDATE LAST LOGIN =================

    @PatchMapping("/{userId}/last-login")
    public ResponseEntity<ApiResponse<Void>> updateLastLogin(
            @PathVariable UUID userId) {

        log.info("API request to update last login for user {}", userId);

        userService.updateLastLogin(userId);

        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Last login updated successfully")
                .data(null)
                .failureMessage(null)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}