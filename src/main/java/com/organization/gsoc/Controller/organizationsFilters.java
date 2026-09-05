package com.organization.gsoc.Controller;

import com.organization.gsoc.DTO.OrganizationFiltersResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping("/api")
public interface organizationsFilters {
    @GetMapping("/all-filters")
    public ResponseEntity<Map<String, OrganizationFiltersResponseDTO>> getOrganizationFilters();
}
