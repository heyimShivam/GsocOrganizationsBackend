package com.organization.gsoc.Controller;

import com.organization.gsoc.DTO.OrganizationContactDTO;
import com.organization.gsoc.DTO.OrganizationDetailsDTO;
import com.organization.gsoc.DTO.OrganizationsResponseDTO;
import com.organization.gsoc.Service.OrganizationService;
import com.organization.gsoc.Service.OrganizationServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
public class OrganizationsImpl implements Organizations {
    private final OrganizationService organizationService;

    public OrganizationsImpl(OrganizationServiceImpl organizationServiceImpl) {
        this.organizationService = organizationServiceImpl;
    }

    public ResponseEntity<Map<String, OrganizationsResponseDTO>> getOrganizations(@RequestParam(name="page", defaultValue = "1") int pageNumber, @RequestParam(name="size", defaultValue = "10") int size) {
        OrganizationsResponseDTO result = organizationService.getOrganizations(pageNumber, size);
        return ResponseEntity.ok().body(Map.of("data", result));
    }

    public ResponseEntity<Map<String, OrganizationDetailsDTO>> getOrganization(@PathVariable UUID id) {
        OrganizationDetailsDTO result = organizationService.getOrganizationById(id);
        return ResponseEntity.ok().body(Map.of("data", result));
    }
}
