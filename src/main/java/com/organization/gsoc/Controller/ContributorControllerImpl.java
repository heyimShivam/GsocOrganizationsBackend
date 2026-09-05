package com.organization.gsoc.Controller;

import com.organization.gsoc.DTO.ContributorsResponseDTO;
import com.organization.gsoc.DTO.OrganizationFiltersResponseDTO;
import com.organization.gsoc.Service.ContributorService;
import com.organization.gsoc.Service.OrganizationFilterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
public class ContributorControllerImpl
        implements ContributorController {

    private final ContributorService contributorService;

    public ContributorControllerImpl(
            ContributorService contributorService
    ) {
        this.contributorService = contributorService;
    }

    @Override
    public ResponseEntity<Map<String, ContributorsResponseDTO>>
    getContributors(

            @PathVariable UUID organizationId,

            @RequestParam(name = "page", defaultValue = "1")
            int page,

            @RequestParam(name = "size", defaultValue = "24")
            int size
    ) {

        ContributorsResponseDTO result =
                contributorService.getContributors(
                        organizationId,
                        page,
                        size
                );

        return ResponseEntity.ok(
                Map.of("data", result)
        );
    }
}