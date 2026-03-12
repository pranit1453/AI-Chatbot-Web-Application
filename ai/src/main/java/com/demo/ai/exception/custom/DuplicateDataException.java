package com.demo.ai.exception.custom;

import com.demo.ai.exception.BaseException;
import org.springframework.http.HttpStatus;

public class DuplicateDataException extends BaseException {
    public DuplicateDataException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
