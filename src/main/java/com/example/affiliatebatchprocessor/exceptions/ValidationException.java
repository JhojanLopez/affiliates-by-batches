package com.example.affiliatebatchprocessor.exceptions;

import lombok.Getter;

@Getter
public class ValidationException extends Throwable {
    private final String field;

    public ValidationException(String message, String field) {
        super(message);
        this.field = field;
    }
}
