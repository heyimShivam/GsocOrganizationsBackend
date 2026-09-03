package com.organization.gsoc.Service;

import com.organization.gsoc.DTO.OrganizationContactDTO;
import com.organization.gsoc.DTO.OrganizationDetailsDTO;
import com.organization.gsoc.DTO.OrganizationsResponseDTO;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface OrganizationService {
    public OrganizationsResponseDTO getOrganizations(int page, int size);
    public OrganizationDetailsDTO getOrganizationById(UUID id);
}
