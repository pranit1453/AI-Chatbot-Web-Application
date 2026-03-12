package com.demo.ai.exception.custom;

import com.demo.ai.exception.BaseException;
import org.springframework.http.HttpStatus;

public class InvalidDataException extends BaseException {
    public InvalidDataException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
