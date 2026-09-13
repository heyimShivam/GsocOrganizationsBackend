package com.organization.gsoc.DTO;

import java.time.Instant;
import java.util.UUID;

public record ChatMessageResponse(
        UUID id,
        UUID channelId,
        UUID userId,
        String userName,
        String githubUsername,
        String message,
        Instant createdAt
) {
}