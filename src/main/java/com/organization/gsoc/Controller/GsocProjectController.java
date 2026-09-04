package com.organization.gsoc.Controller;

import com.organization.gsoc.DTO.GsocProjectFilterRequest;
import com.organization.gsoc.DTO.GsocProjectsResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RequestMapping("/api")
public interface GsocProjectController {

    @GetMapping("/projects/{organizationId}")
    ResponseEntity<Map<String, GsocProjectsResponseDTO>> getProjects(

            @PathVariable UUID organizationId,

            @RequestParam(name = "page", defaultValue = "1")
            int page,

            @RequestParam(name = "size", defaultValue = "24")
            int size,

            @RequestBody(required = false)
            GsocProjectFilterRequest filterRequest
    );
}