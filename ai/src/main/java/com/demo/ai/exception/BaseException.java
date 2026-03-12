package com.demo.ai.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public class BaseException extends RuntimeException{
    private final HttpStatus status;

    public BaseException(String message, HttpStatus status){
        super(message);
        this.status = status;
    }
}
