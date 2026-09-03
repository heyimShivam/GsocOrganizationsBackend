package com.organization.gsoc.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoPageException.class)
    public ResponseEntity<Map<String, String>> handleNoPage(
            NoPageException ex) {

        return ResponseEntity
                .badRequest()
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(OrganizationNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleOrganizationNotFound(
            OrganizationNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {

        if (ex.getRequiredType() == UUID.class) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message",
                            "Invalid organization ID. Please provide a valid UUID."
                    ));
        }

        return ResponseEntity
                .badRequest()
                .body(Map.of(
                        "message",
                        "Invalid request parameter."
                ));
    }
}