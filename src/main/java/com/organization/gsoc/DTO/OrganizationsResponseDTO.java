package com.organization.gsoc.DTO;

import java.util.List;
import java.util.UUID;

public record OrganizationsResponseDTO(
        int page,
        int size,
        List<OrganizationSummaryDTO> content,
        long totalRecords,
        int totalPages,
        boolean first,
        boolean last
) {
}
