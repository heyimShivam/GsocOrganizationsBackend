package com.organization.gsoc.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(

        @NotBlank
        @Size(max = 255)
        String name,

        @Size(max = 2000)
        String description,

        @Size(max = 1000)
        String quote,

        @Size(max = 255)
        String githubUsername
) {
}