package com.organization.gsoc.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ContactMessageReplyRequest(

        @NotBlank(message = "Reply is required")
        @Size(max = 10000, message = "Reply must not exceed 10000 characters")
        String adminReply
) {
}