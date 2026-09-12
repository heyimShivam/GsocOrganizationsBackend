package com.organization.gsoc.DTO;

import com.organization.gsoc.Enums.ContactMessageStatus;
import jakarta.validation.constraints.NotNull;

public record ContactMessageStatusRequest(

        @NotNull(message = "Status is required")
        ContactMessageStatus status
) {
}