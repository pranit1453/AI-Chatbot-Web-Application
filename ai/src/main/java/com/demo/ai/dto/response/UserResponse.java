package com.demo.ai.dto.response;

import com.demo.ai.model.enums.Role;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(

        UUID userId,
        String username,
        String email,
        Role role,
        Instant createdAt

) {}