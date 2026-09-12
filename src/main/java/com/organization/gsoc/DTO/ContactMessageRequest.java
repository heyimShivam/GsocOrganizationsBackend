package com.organization.gsoc.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ContactMessageRequest(

        @NotBlank(message = "Name is required")
        @Size(max = 255, message = "Name must not exceed 255 characters")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email address")
        @Size(max = 255, message = "Email must not exceed 255 characters")
        String email,

        @NotBlank(message = "Subject is required")
        @Size(max = 255, message = "Subject must not exceed 255 characters")
        String subject,

        @NotBlank(message = "Message is required")
        @Size(max = 10000, message = "Message must not exceed 10000 characters")
        String message
) {
}