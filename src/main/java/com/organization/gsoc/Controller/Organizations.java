package com.organization.gsoc.Controller;

import com.organization.gsoc.DTO.OrganizationContactDTO;
import com.organization.gsoc.DTO.OrganizationDetailsDTO;
import com.organization.gsoc.DTO.OrganizationFilterDTO;
import com.organization.gsoc.DTO.OrganizationsResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RequestMapping("/api/organizations")
public interface Organizations {
    @PostMapping("")
    public ResponseEntity<Map<String, OrganizationsResponseDTO>> getOrganizations(@RequestBody(required = false) OrganizationFilterDTO search, @RequestParam(name="page", defaultValue = "0") int pageNumber, @RequestParam(name="size", defaultValue = "10") int size);

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, OrganizationDetailsDTO>> getOrganizationById(@PathVariable UUID id);
}
