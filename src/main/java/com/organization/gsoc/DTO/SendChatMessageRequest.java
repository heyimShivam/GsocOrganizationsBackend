package com.organization.gsoc.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record SendChatMessageRequest(

        @NotNull(message = "Channel ID is required")
        UUID channelId,

        @NotBlank(message = "Message cannot be empty")
        @Size(
                max = 1000,
                message = "Message cannot exceed 1000 characters"
        )
        String message
) {
}