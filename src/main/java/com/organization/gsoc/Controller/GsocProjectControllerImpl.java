package com.organization.gsoc.Controller;

import com.organization.gsoc.DTO.GsocProjectFilterRequest;
import com.organization.gsoc.DTO.GsocProjectsResponseDTO;
import com.organization.gsoc.Service.GsocProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
public class GsocProjectControllerImpl
        implements GsocProjectController {

    private final GsocProjectService gsocProjectService;

    public GsocProjectControllerImpl(
            GsocProjectService gsocProjectService
    ) {
        this.gsocProjectService = gsocProjectService;
    }

    @Override
    public ResponseEntity<Map<String, GsocProjectsResponseDTO>>
    getProjects(

            @PathVariable UUID organizationId,

            @RequestParam(name = "page", defaultValue = "1")
            int page,

            @RequestParam(name = "size", defaultValue = "24")
            int size,

            @RequestBody(required = false)
            GsocProjectFilterRequest filterRequest
    ) {

        if (filterRequest == null) {
            filterRequest = new GsocProjectFilterRequest();
        }

        GsocProjectsResponseDTO result =
                gsocProjectService.getProjects(
                        organizationId,
                        page,
                        size,
                        filterRequest
                );

        return ResponseEntity.ok(
                Map.of("data", result)
        );
    }
}