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

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<Map<String, String>> handlePasswordMismatch(
            PasswordMismatchException ex
    ) {
        return ResponseEntity
                .badRequest()
                .body(Map.of(
                        "message", ex.getMessage()
                ));
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExists(
            EmailAlreadyExistsException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "message", ex.getMessage()
                ));
    }

    @ExceptionHandler(InvalidVerificationTokenException.class)
    public ResponseEntity<Map<String, String>> handleInvalidVerificationToken(
            InvalidVerificationTokenException ex
    ) {
        return ResponseEntity
                .badRequest()
                .body(Map.of(
                        "message",
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(VerificationTokenAlreadyUsedException.class)
    public ResponseEntity<Map<String, String>> handleVerificationTokenAlreadyUsed(
            VerificationTokenAlreadyUsedException ex
    ) {
        return ResponseEntity
                .badRequest()
                .body(Map.of(
                        "message",
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(VerificationTokenExpiredException.class)
    public ResponseEntity<Map<String, String>> handleVerificationTokenExpired(
            VerificationTokenExpiredException ex
    ) {
        return ResponseEntity
                .badRequest()
                .body(Map.of(
                        "message",
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCredentials(
            InvalidCredentialsException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(EmailNotVerifiedException.class)
    public ResponseEntity<Map<String, String>> handleEmailNotVerified(
            EmailNotVerifiedException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(AuthenticatedUserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleAuthenticatedUserNotFound(
            AuthenticatedUserNotFoundException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "message",
                        ex.getMessage()
                ));
    }
}