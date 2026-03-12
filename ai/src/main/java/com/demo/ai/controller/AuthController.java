package com.demo.ai.controller;

import com.demo.ai.dto.request.LoginRequest;
import com.demo.ai.wrapper.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ApiResponse<String> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {

        log.info("Login request for {}", request.username());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        httpRequest.getSession(true); // create session

        return ApiResponse.<String>builder()
                .success(true)
                .status(200)
                .message("Login successful")
                .data("/dashboard")
                .timestamp(Instant.now())
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(HttpServletRequest request) {

        request.getSession().invalidate();

        return ApiResponse.<String>builder()
                .success(true)
                .status(200)
                .message("Logout successful")
                .data(null)
                .timestamp(Instant.now())
                .build();
    }
}