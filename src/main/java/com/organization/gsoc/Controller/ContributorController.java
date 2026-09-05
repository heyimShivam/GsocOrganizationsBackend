package com.organization.gsoc.Controller;

import com.organization.gsoc.DTO.ContributorsResponseDTO;
import com.organization.gsoc.DTO.OrganizationFiltersResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.UUID;

public interface ContributorController {

    @GetMapping("/api/organizations/{organizationId}/contributors")
    ResponseEntity<Map<String, ContributorsResponseDTO>> getContributors(

            @PathVariable UUID organizationId,

            @RequestParam(name = "page", defaultValue = "1")
            int page,

            @RequestParam(name = "size", defaultValue = "24")
            int size
    );
}