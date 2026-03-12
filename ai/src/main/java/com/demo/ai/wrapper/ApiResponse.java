package com.demo.ai.wrapper;

import lombok.Builder;

import java.time.Instant;

@Builder
public record ApiResponse<T>(
        int status,
        boolean success,
        String message,
        T data,
        String failureMessage,
        Instant timestamp
) {}