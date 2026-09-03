package com.organization.gsoc.DTO;

import com.organization.gsoc.Enums.SortBy;
import com.organization.gsoc.Enums.SortDirection;

import java.util.List;

public record OrganizationFilterDTO(
        String orgName,
        List<Integer> years,
        List<String> categories,
        List<String> topics,
        List<String> technologies,
        Boolean activeOrg,
        SortBy sortBy,
        SortDirection sortDirection
) {
    public OrganizationFilterDTO {
        if (orgName == null) {
            orgName = "";
        }

        if (years == null) {
            years = List.of();
        }

        if (categories == null) {
            categories = List.of();
        }

        if (topics == null) {
            topics = List.of();
        }

        if (technologies == null) {
            technologies = List.of();
        }

        if (sortDirection == null) {
            sortDirection = SortDirection.ASC;
        }

        if (sortBy == null) {
            sortBy = SortBy.NAME;
        }
    }
}