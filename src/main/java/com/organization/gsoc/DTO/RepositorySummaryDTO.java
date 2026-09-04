package com.organization.gsoc.DTO;

import java.util.List;
import java.util.UUID;

public record RepositorySummaryDTO(
        UUID id,
        String name,
        String fullName,
        String htmlUrl,
        String description,
        String language,
        int stars,
        int forks,
        int openIssues,
        List<String> topics
) {
}