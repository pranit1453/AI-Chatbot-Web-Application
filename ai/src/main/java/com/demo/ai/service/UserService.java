package com.demo.ai.service;

import com.demo.ai.dto.request.CreateUserRequest;
import com.demo.ai.dto.request.UpdateUserRequest;
import com.demo.ai.dto.response.UserResponse;
import java.util.UUID;

public interface UserService {
    UserResponse createUser(CreateUserRequest request);

    UserResponse getUserById(UUID userId);

    UserResponse updateUser(UUID userId, UpdateUserRequest request);

    void deleteUser(UUID userId); // soft delete

    UserResponse restoreUser(UUID userId);

    void permanentlyDelete(UUID userId);

    void updateLastLogin(UUID userId);
}
