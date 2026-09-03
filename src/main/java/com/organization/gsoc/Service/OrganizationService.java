package com.organization.gsoc.Service;

import com.organization.gsoc.DTO.*;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface OrganizationService {
    public OrganizationsResponseDTO getOrganizations(OrganizationFilterDTO search, int page, int size);
    public OrganizationDetailsDTO getOrganizationById(UUID id);
}
