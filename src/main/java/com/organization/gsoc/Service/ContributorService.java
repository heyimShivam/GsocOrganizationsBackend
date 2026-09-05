package com.organization.gsoc.Service;

import com.organization.gsoc.DTO.ContributorsResponseDTO;

import java.util.UUID;

public interface ContributorService {

    ContributorsResponseDTO getContributors(
            UUID organizationId,
            int page,
            int size
    );
}