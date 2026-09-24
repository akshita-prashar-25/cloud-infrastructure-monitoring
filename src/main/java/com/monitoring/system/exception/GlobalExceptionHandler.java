package com.monitoring.system.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(
            MethodArgumentNotValidException exception) {

        Map<String, String> errors =
                new LinkedHashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put(
                "status",
                HttpStatus.BAD_REQUEST.value()
        );

        response.put(
                "message",
                "Validation failed"
        );

        response.put(
                "errors",
                errors
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleMalformedRequest(
            HttpMessageNotReadableException exception) {

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put(
                "status",
                HttpStatus.BAD_REQUEST.value()
        );

        response.put(
                "message",
                "Invalid request body"
        );

        response.put(
                "error",
                "Request body is missing or contains invalid JSON"
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDatabaseError(
            DataIntegrityViolationException exception) {

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put(
                "status",
                HttpStatus.CONFLICT.value()
        );

        response.put(
                "message",
                "Database operation failed"
        );

        response.put(
                "error",
                "The requested operation conflicts with existing data"
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(
            Exception exception) {

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put(
                "status",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );

        response.put(
                "message",
                "Internal server error"
        );

        response.put(
                "error",
                "An unexpected error occurred"
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFound(
            ResourceNotFoundException exception) {

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put(
                "status",
                HttpStatus.NOT_FOUND.value()
        );

        response.put(
                "message",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }
}