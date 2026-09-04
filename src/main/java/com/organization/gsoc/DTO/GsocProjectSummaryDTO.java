package com.organization.gsoc.DTO;

import java.util.UUID;

public record GsocProjectSummaryDTO(

        UUID id,

        int year,

        String title,

        String shortDescription,

        String studentName,

        String codeUrl,

        String proposalId,

        String projectUrl

) {
}