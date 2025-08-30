package com.zoo.error;

import org.springframework.validation.FieldError;

import java.time.Instant;
import java.util.List;

public class ErrorResponse {

    public Instant timestamp = Instant.now();
    public int status;
    public String error;
    public String path;
    public String message;
    public List<FieldError> fieldErrors; // validation detayları

    public static class FieldError{
        public String field;
        public String message;
        public FieldError(String field, String message) {
            this.field = field; this.message = message;
        }
    }
}
