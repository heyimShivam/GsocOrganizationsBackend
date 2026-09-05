package com.organization.gsoc.DTO;

import java.util.UUID;

public record ContributorSummaryDTO(

        UUID id,

        Long githubUserId,

        String githubNodeId,

        String login,

        String avatarUrl,

        String htmlUrl,

        int contributions

) {
}