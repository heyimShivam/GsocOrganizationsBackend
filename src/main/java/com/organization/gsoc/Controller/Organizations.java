package com.organization.gsoc.Controller;

import com.organization.gsoc.DTO.OrganizationContactDTO;
import com.organization.gsoc.DTO.OrganizationDetailsDTO;
import com.organization.gsoc.DTO.OrganizationsResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.UUID;

@RequestMapping("/api/organizations")
public interface Organizations {
    @GetMapping("")
    public ResponseEntity<Map<String, OrganizationsResponseDTO>> getOrganizations(@RequestParam(required = false) String search, @RequestParam(name="page", defaultValue = "0") int pageNumber, @RequestParam(name="size", defaultValue = "10") int size);

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, OrganizationDetailsDTO>> getOrganization(@PathVariable UUID id);
}
