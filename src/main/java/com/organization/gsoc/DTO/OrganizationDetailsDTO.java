package com.organization.gsoc.DTO;

import java.util.List;
import java.util.UUID;

public record OrganizationDetailsDTO(
        UUID id,
        String name,
        String imageUrl,
        String imageBackgroundColor,
        String description,
        String url,
        String githubId,
        boolean activeOrg,
        List<Integer> years,
        List<String> categories,
        List<String> topics,
        List<String> technologies,
        OrganizationContactDTO contactInfo
) {
}
