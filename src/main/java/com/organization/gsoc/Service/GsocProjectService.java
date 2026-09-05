package com.organization.gsoc.Service;

import com.organization.gsoc.DTO.GsocProjectFilterRequest;
import com.organization.gsoc.DTO.GsocProjectYearSummaryDTO;
import com.organization.gsoc.DTO.GsocProjectsResponseDTO;

import java.util.List;
import java.util.UUID;

public interface GsocProjectService {

    GsocProjectsResponseDTO getProjects(
            UUID organizationId,
            int page,
            int size,
            GsocProjectFilterRequest filterRequest
    );

//    new
    List<GsocProjectYearSummaryDTO> getProjectYears(
            UUID organizationId
    );
}