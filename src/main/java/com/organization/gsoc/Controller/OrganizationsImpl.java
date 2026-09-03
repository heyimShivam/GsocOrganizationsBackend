package com.organization.gsoc.Controller;

import com.organization.gsoc.DTO.*;
import com.organization.gsoc.Enums.SortBy;
import com.organization.gsoc.Enums.SortDirection;
import com.organization.gsoc.Service.OrganizationService;
import com.organization.gsoc.Service.OrganizationServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class OrganizationsImpl implements Organizations {
    private final OrganizationService organizationService;

    public OrganizationsImpl(OrganizationServiceImpl organizationServiceImpl) {
        this.organizationService = organizationServiceImpl;
    }

    public ResponseEntity<Map<String, OrganizationsResponseDTO>> getOrganizations(
            @RequestBody(required = false) OrganizationFilterDTO search,
            @RequestParam(name = "page", defaultValue = "1") int pageNumber,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        search = (search == null ? (new OrganizationFilterDTO(
                "", List.of(), List.of(), List.of(), List.of(), null, SortBy.NAME, SortDirection.ASC)
        ) : search);
        OrganizationsResponseDTO result = organizationService.getOrganizations(search, pageNumber, size);
        return ResponseEntity.ok().body(Map.of("data", result));
    }

    public ResponseEntity<Map<String, OrganizationDetailsDTO>> getOrganizationById(@PathVariable UUID id) {
        System.out.println("wor");
        OrganizationDetailsDTO result = organizationService.getOrganizationById(id);
        System.out.println("workkks");
        return ResponseEntity.ok().body(Map.of("data", result));
    }
}
