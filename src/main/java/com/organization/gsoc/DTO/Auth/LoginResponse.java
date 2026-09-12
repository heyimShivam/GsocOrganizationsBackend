package com.organization.gsoc.DTO.Auth;

import java.util.UUID;

public record LoginResponse(
        UUID id,
        String name,
        String email,
        String role
) {
}