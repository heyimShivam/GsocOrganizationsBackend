package com.organization.gsoc.Service;

import com.organization.gsoc.DTO.RepositoriesResponseDTO;
import com.organization.gsoc.DTO.RepositoryFilterRequest;

import java.util.UUID;

public interface RepositoryService {
    public RepositoriesResponseDTO getRepositories(UUID organizationId, int pageNumber, int size, RepositoryFilterRequest filterRequest);
}
