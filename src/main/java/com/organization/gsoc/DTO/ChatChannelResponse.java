package com.organization.gsoc.DTO;

import java.util.UUID;

public record ChatChannelResponse(
        UUID id,
        String name,
        String description
) {
}