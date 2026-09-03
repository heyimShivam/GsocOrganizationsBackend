package com.organization.gsoc.DTO;

import java.util.List;
import java.util.UUID;

public record OrganizationSummaryDTO(
        UUID id,
        String name,
        String imageUrl,
        String description,
        String githubID,
        boolean activeOrg
) {
}