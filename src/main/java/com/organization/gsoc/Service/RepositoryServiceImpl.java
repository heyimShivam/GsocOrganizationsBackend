package com.organization.gsoc.Service;

import com.organization.gsoc.DTO.OrganizationsResponseDTO;
import com.organization.gsoc.DTO.RepositoriesResponseDTO;
import com.organization.gsoc.DTO.RepositoryFilterRequest;
import com.organization.gsoc.DTO.RepositorySummaryDTO;
import com.organization.gsoc.Entity.OrganizationEntity;
import com.organization.gsoc.Entity.RepositoryEntity;
import com.organization.gsoc.Enums.SortDirection;
import com.organization.gsoc.Exception.NoPageException;
import com.organization.gsoc.Repository.RepositoryRepository;
import com.organization.gsoc.Repository.RepositoryTopicRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RepositoryServiceImpl implements RepositoryService {
    private final RepositoryRepository repositoryRepository;
    private final RepositoryTopicRepository repositoryTopicRepository;

    public RepositoryServiceImpl(
            RepositoryRepository repositoryRepository,
            RepositoryTopicRepository repositoryTopicRepository) {
        this.repositoryRepository = repositoryRepository;
        this.repositoryTopicRepository = repositoryTopicRepository;

    }

    @Override
    public RepositoriesResponseDTO getRepositories(UUID organizationId, int page, int size, RepositoryFilterRequest filterRequest) {
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

        String search = filterRequest != null ? filterRequest.repoName() : "";
        String sortField = switch (filterRequest.sortBy()) {
            case STARS -> "stars";
            case FORKS -> "forks";
            case OPEN_ISSUES -> "openIssues";
        };
        Sort.Direction sortDirection =
                Sort.Direction.valueOf(filterRequest.direction().name());

        PageRequest pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by(sortDirection, sortField)
        );

        Page<RepositoryEntity> data;

        if (search != null && !search.trim().isEmpty()) {

            data = repositoryRepository
                    .findRepositoriesByOrganizationId(
                            organizationId,
                            search.trim(),
                            pageable
                    );

        } else {

            data = repositoryRepository
                    .findRepositoriesByOrganizationId(
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

        List<RepositorySummaryDTO> reposData = data
                .stream()
                .map(repository -> {
                    List<String> topics =
                            repositoryTopicRepository
                                    .findTopicNamesByRepositoryId(repository.getId());

                    return new RepositorySummaryDTO(
                            repository.getId(),
                            repository.getName(),
                            repository.getFullName(),
                            repository.getHtmlUrl(),
                            repository.getDescription(),
                            repository.getLanguage(),
                            repository.getStars(),
                            repository.getForks(),
                            repository.getOpenIssues(),
                            topics
                    );
                })
                .toList();


        return new RepositoriesResponseDTO(
                reposData,
                data.getNumber() + 1,
                data.getSize(),
                data.getTotalElements(),
                data.getTotalPages(),
                data.isFirst(),
                data.isLast()
        );
    }
}
