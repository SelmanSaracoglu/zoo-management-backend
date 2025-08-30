package com.zoo.error;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 – Body JSON'ı bozuk/eksik
    @org.springframework.web.bind.annotation.ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleBadJson(HttpMessageNotReadableException ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, req, "Malformed JSON request", null);
    }

    // 400 – @Valid (body) alan hataları
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgNotValid(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<ErrorResponse.FieldError> fields = new ArrayList<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(fe -> fields.add(new ErrorResponse.FieldError(fe.getField(), fe.getDefaultMessage())));
        return build(HttpStatus.BAD_REQUEST, req, "Validation failed", fields);
    }

    // 400 – @Validated (query/path) alan hataları
    @org.springframework.web.bind.annotation.ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest req) {
        List<ErrorResponse.FieldError> fields = new ArrayList<>();
        ex.getConstraintViolations().forEach(v -> {
            String field = v.getPropertyPath() == null ? "" : v.getPropertyPath().toString();
            fields.add(new ErrorResponse.FieldError(field, v.getMessage()));
        });
        return build(HttpStatus.BAD_REQUEST, req, "Validation failed", fields);
    }

    // 400 – BindException (bazı parametre binding senaryoları)
    @org.springframework.web.bind.annotation.ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBind(BindException ex, HttpServletRequest req) {
        List<ErrorResponse.FieldError> fields = new ArrayList<>();
        ex.getFieldErrors().forEach(fe -> fields.add(new ErrorResponse.FieldError(fe.getField(), fe.getDefaultMessage())));
        return build(HttpStatus.BAD_REQUEST, req, "Validation failed", fields);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(com.zoo.error.NotFoundException.class)
    public org.springframework.http.ResponseEntity<ErrorResponse>
    handleNotFound(com.zoo.error.NotFoundException ex,
                   jakarta.servlet.http.HttpServletRequest req) {
        return build(org.springframework.http.HttpStatus.NOT_FOUND, req, ex.getMessage(), null);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(com.zoo.error.ConflictException.class)
    public org.springframework.http.ResponseEntity<ErrorResponse>
    handleConflict(com.zoo.error.ConflictException ex,
                   jakarta.servlet.http.HttpServletRequest req) {
        return build(org.springframework.http.HttpStatus.CONFLICT, req, ex.getMessage(), null);
    }

    // 500 – yakalanmayan her şey
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOther(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, req, "Unexpected error", null);
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, HttpServletRequest req, String message, List<ErrorResponse.FieldError> fields) {
        ErrorResponse er = new ErrorResponse();
        er.status = status.value();
        er.error  = status.getReasonPhrase();
        er.path   = req.getRequestURI();
        er.message = message;
        er.fieldErrors = fields;
        return ResponseEntity.status(status).body(er);
    }
}
