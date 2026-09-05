package com.organization.gsoc.DTO;

import java.util.List;

public record OrganizationFiltersResponseDTO(
        List<String> categories,
        List<String> topics,
        List<String> technologies,
        List<Integer> years
) {
}