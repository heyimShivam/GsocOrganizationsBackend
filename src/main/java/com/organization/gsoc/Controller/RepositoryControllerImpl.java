package com.organization.gsoc.Controller;

import com.organization.gsoc.DTO.RepositoriesResponseDTO;
import com.organization.gsoc.DTO.RepositoryFilterRequest;
import com.organization.gsoc.Service.RepositoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;

@RestController
public class RepositoryControllerImpl implements RepositoryController {
    private final RepositoryService repositoryService;

    public RepositoryControllerImpl(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @Override
    public ResponseEntity<Map<String, RepositoriesResponseDTO>> getRepositories(
            @PathVariable UUID organizationId,
            @RequestParam(name = "page", defaultValue = "1") int pageNumber,
            @RequestParam(name = "size", defaultValue = "24") int size,
            @RequestBody(required = false) RepositoryFilterRequest filterRequest
    ) {
        if(filterRequest == null) filterRequest = new RepositoryFilterRequest();
        RepositoriesResponseDTO result = repositoryService.getRepositories(organizationId, pageNumber, size, filterRequest);
        return ResponseEntity.status(OK).body(Map.of("data", result));
    }
}
