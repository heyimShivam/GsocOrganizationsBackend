package com.organization.gsoc.Service;

import com.organization.gsoc.DTO.GsocProjectFilterRequest;
import com.organization.gsoc.DTO.GsocProjectSummaryDTO;
import com.organization.gsoc.DTO.GsocProjectsResponseDTO;
import com.organization.gsoc.Entity.GsocProjectEntity;
import com.organization.gsoc.Exception.NoPageException;
import com.organization.gsoc.Repository.GsocProjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GsocProjectServiceImpl
        implements GsocProjectService {

    private final GsocProjectRepository gsocProjectRepository;

    public GsocProjectServiceImpl(
            GsocProjectRepository gsocProjectRepository
    ) {
        this.gsocProjectRepository = gsocProjectRepository;
    }

    @Override
    public GsocProjectsResponseDTO getProjects(
            UUID organizationId,
            int page,
            int size,
            GsocProjectFilterRequest filterRequest
    ) {

        if (page < 1) {
            throw new NoPageException(
                    "Page number must be greater than or equal to 1"
            );
        }

        if (size < 1) {
            throw new IllegalArgumentException(
                    "Size must be greater than 0"
            );
        }

        PageRequest pageable = PageRequest.of(
                page - 1,
                size
        );

        String title = filterRequest.title();
        Integer year = filterRequest.year();

        Page<GsocProjectEntity> data;

        if (year != null) {

            data = gsocProjectRepository
                    .findByOrganizationIdAndYear(
                            organizationId,
                            year,
                            pageable
                    );

        } else {

            data = gsocProjectRepository
                    .findByOrganizationId(
                            organizationId,
                            pageable
                    );
        }

        if (page > data.getTotalPages()
                && data.getTotalPages() > 0) {

            throw new NoPageException(
                    "Page " + page +
                            " does not exist. Total pages available: " +
                            data.getTotalPages()
            );
        }

        List<GsocProjectSummaryDTO> projects =
                data.stream()
                        .map(project ->
                                new GsocProjectSummaryDTO(
                                        project.getId(),
                                        project.getYear(),
                                        project.getTitle(),
                                        project.getShortDescription(),
                                        project.getStudentName(),
                                        project.getCodeUrl(),
                                        project.getProposalId(),
                                        project.getProjectUrl()
                                )
                        )
                        .toList();

        return new GsocProjectsResponseDTO(
                projects,
                data.getNumber() + 1,
                data.getSize(),
                data.getTotalElements(),
                data.getTotalPages(),
                data.isFirst(),
                data.isLast()
        );
    }
}