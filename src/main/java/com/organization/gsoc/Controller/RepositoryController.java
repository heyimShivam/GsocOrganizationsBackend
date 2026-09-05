package com.organization.gsoc.Controller;

import com.organization.gsoc.DTO.OrganizationsResponseDTO;
import com.organization.gsoc.DTO.RepositoriesResponseDTO;
import com.organization.gsoc.DTO.RepositoryFilterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RequestMapping("/api")
public interface RepositoryController {
    @PostMapping("/repositories/{organizationId}")
    public ResponseEntity<Map<String, RepositoriesResponseDTO>> getRepositories(
            @PathVariable UUID organizationId,
            @RequestParam(name = "page", defaultValue = "1") int pageNumber,
            @RequestParam(name = "size", defaultValue = "24") int size,
            @RequestBody(required = false) RepositoryFilterRequest filterRequest
    );
}
