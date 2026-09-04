package com.organization.gsoc.Service;

import com.organization.gsoc.DTO.GsocProjectFilterRequest;
import com.organization.gsoc.DTO.GsocProjectsResponseDTO;

import java.util.UUID;

public interface GsocProjectService {

    GsocProjectsResponseDTO getProjects(
            UUID organizationId,
            int page,
            int size,
            GsocProjectFilterRequest filterRequest
    );
}