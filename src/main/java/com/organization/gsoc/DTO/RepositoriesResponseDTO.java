package com.organization.gsoc.DTO;

import java.util.List;

public record RepositoriesResponseDTO(
        List<RepositorySummaryDTO> content,
        int page,
        int size,
        long totalRecords,
        int totalPages,
        boolean first,
        boolean last
) {
}