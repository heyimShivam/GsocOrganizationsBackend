package com.organization.gsoc.Controller;

import com.organization.gsoc.DTO.OrganizationFiltersResponseDTO;
import com.organization.gsoc.Service.OrganizationFilterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class organizationsFiltersImpl implements organizationsFilters{
    private final OrganizationFilterService organizationFilterService;

    organizationsFiltersImpl(OrganizationFilterService organizationFilterService) {
        this.organizationFilterService = organizationFilterService;
    }

    public ResponseEntity<Map<String, OrganizationFiltersResponseDTO>> getOrganizationFilters() {

        OrganizationFiltersResponseDTO filters =
                organizationFilterService.getFilters();

        return ResponseEntity.ok(
                Map.of("data", filters)
        );
    }
}
