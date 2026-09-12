package com.organization.gsoc.DTO;

import com.organization.gsoc.Enums.ContactMessageStatus;

import java.time.Instant;
import java.util.UUID;

public record ContactMessageResponse(
        UUID id,
        String name,
        String email,
        String subject,
        String message,
        ContactMessageStatus status,
        Instant createdAt
) {
}