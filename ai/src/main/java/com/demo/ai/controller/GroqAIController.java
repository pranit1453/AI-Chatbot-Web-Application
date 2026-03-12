package com.demo.ai.controller;

import com.demo.ai.wrapper.ApiResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api/groq")
public class GroqAIController {

    private final ChatClient chatClient;

    public GroqAIController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/{message}")
    public ApiResponse<String> getAnswer(@PathVariable String message) {

        String response = chatClient
                .prompt()
                .user(message)
                .call()
                .content();
        return ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("AI response generated successfully")
                .data(response)
                .failureMessage(null)
                .timestamp(Instant.now())
                .build();

    }
}