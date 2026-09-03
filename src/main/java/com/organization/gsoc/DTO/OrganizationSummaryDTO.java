package com.organization.gsoc.DTO;

import java.util.List;
import java.util.UUID;

public record OrganizationSummaryDTO(
        UUID id,
        String name,
        String imageUrl,
        String description,
        String githubID,
        String imageBackgroundColor,
        boolean activeOrg,
        List<String> technologies,
        List<Integer> years
) {
}