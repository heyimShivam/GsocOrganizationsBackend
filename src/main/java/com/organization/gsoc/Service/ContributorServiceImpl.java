package com.organization.gsoc.Service;

import com.organization.gsoc.DTO.ContributorSummaryDTO;
import com.organization.gsoc.DTO.ContributorsResponseDTO;
import com.organization.gsoc.Exception.NoPageException;
import com.organization.gsoc.Repository.Projection.ContributorProjection;
import com.organization.gsoc.Repository.ContributorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ContributorServiceImpl
        implements ContributorService {

    private final ContributorRepository contributorRepository;

    public ContributorServiceImpl(
            ContributorRepository contributorRepository
    ) {
        this.contributorRepository = contributorRepository;
    }

    @Override
    public ContributorsResponseDTO getContributors(
            UUID organizationId,
            int page,
            int size
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

        PageRequest pageable =
                PageRequest.of(page - 1, size,
                        Sort.by(
                                Sort.Direction.DESC,
                                "contributions"
                        ));

        Page<ContributorProjection> data =
                contributorRepository
                        .findContributorsByOrganizationId(
                                organizationId,
                                pageable
                        );

        if (page > data.getTotalPages()
                && data.getTotalPages() > 0) {

            throw new NoPageException(
                    "Page " + page +
                            " does not exist. Total pages available: " +
                            data.getTotalPages()
            );
        }

        List<ContributorSummaryDTO> contributors =
                data.stream()
                        .map(contributor ->
                                new ContributorSummaryDTO(
                                        contributor.getId(),
                                        contributor.getGithubUserId(),
                                        contributor.getGithubNodeId(),
                                        contributor.getLogin(),
                                        contributor.getAvatarUrl(),
                                        contributor.getHtmlUrl(),
                                        contributor.getContributions()
                                )
                        )
                        .toList();

        return new ContributorsResponseDTO(
                contributors,
                data.getNumber() + 1,
                data.getSize(),
                data.getTotalElements(),
                data.getTotalPages(),
                data.isFirst(),
                data.isLast()
        );
    }
}