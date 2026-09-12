package com.organization.gsoc.DTO;

import com.organization.gsoc.Enums.ContactMessageStatus;

import java.time.Instant;
import java.util.UUID;

public record AdminContactMessageResponse(
        UUID id,
        String name,
        String email,
        String subject,
        String message,
        ContactMessageStatus status,
        String adminReply,
        Instant repliedAt,
        Instant createdAt,
        Instant updatedAt
) {
}