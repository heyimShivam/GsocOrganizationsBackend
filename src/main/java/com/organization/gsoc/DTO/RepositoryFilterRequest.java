package com.organization.gsoc.DTO;

import com.organization.gsoc.Enums.RepositorySortBy;
import com.organization.gsoc.Enums.SortDirection;

public record RepositoryFilterRequest(
        String repoName,
        RepositorySortBy sortBy,
        SortDirection direction
) {

    public RepositoryFilterRequest {
        if (sortBy == null) {
            sortBy = RepositorySortBy.STARS;
        }

        if(repoName == null) {
            repoName = "";
        }

        if (direction == null) {
            direction = SortDirection.DESC;
        }
    }

    public RepositoryFilterRequest() {
        this("", RepositorySortBy.STARS, SortDirection.DESC);
    }
}