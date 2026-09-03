package com.organization.gsoc.Service;

import com.organization.gsoc.DTO.*;
import com.organization.gsoc.Entity.OrganizationEntity;
import com.organization.gsoc.Enums.SortDirection;
import com.organization.gsoc.Exception.NoPageException;
import com.organization.gsoc.Exception.OrganizationNotFoundException;
import com.organization.gsoc.Repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final OrganizationCategoryRepository organizationCategoryRepository;
    private final OrganizationContactRepository organizationContactRepository;
    private final OrganizationTechnologyRepository organizationTechnologyRepository;
    private final OrganizationTopicRepository organizationTopicRepository;
    private final OrganizationYearRepository organizationYearRepository;

    public OrganizationServiceImpl(
            OrganizationRepository organizationRepository,
            OrganizationCategoryRepository organizationCategoryRepository,
            OrganizationContactRepository organizationContactRepository,
            OrganizationTechnologyRepository organizationTechnologyRepository,
            OrganizationTopicRepository organizationTopicRepository,
            OrganizationYearRepository organizationYearRepository
    ) {
        this.organizationRepository = organizationRepository;
        this.organizationCategoryRepository = organizationCategoryRepository;
        this.organizationContactRepository = organizationContactRepository;
        this.organizationTechnologyRepository = organizationTechnologyRepository;
        this.organizationTopicRepository = organizationTopicRepository;
        this.organizationYearRepository = organizationYearRepository;
    }

    public OrganizationsResponseDTO getOrganizations(OrganizationFilterDTO filter, int page, int size) {
        String filterByOrgName = filter.orgName();
        if (page < 1) {
            throw new NoPageException(
                    "Page number must be greater than or equal to 1"
            );
        }

        Sort.Direction direction =
                filter.sortDirection() == SortDirection.DESC
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        // To be implemented in future
        String sortField = switch (filter.sortBy()) {
            case NAME -> "name";
            case POPULARITY -> "name";
            case FREQUENT_SEARCH -> "name";
        };

        PageRequest pageable = PageRequest.of(page - 1, size, Sort.by(direction, sortField));

        Page<OrganizationEntity> organizationPage;

        if(filterByOrgName == null || filterByOrgName.isBlank()) {
            organizationPage = organizationRepository.findAll(pageable);
        } else {
            organizationPage = organizationRepository.findByNameContainingIgnoreCase(filterByOrgName.trim(), pageable);
        }

        if (page > organizationPage.getTotalPages()
                && organizationPage.getTotalPages() > 0) {
            throw new NoPageException(
                    "Page " + page +
                            " does not exist. Total pages available: " +
                            organizationPage.getTotalPages()
            );
        }

        List<OrganizationEntity> organizations =
                organizationPage.getContent();

        List<UUID> organizationIds =
                organizations.stream()
                        .map(OrganizationEntity::getId)
                        .toList();
        List<Object[]> yearResults =
                organizationYearRepository
                        .findYearsByOrganizationIds(organizationIds);

        List<Object[]> technologyResults =
                organizationTechnologyRepository
                        .findTechnologyNamesByOrganizationIds(organizationIds);
        Map<UUID, List<Integer>> yearsMap = new HashMap<>();

        for (Object[] row : yearResults) {
            UUID organizationId = (UUID) row[0];
            Integer year = (Integer) row[1];

            yearsMap
                    .computeIfAbsent(
                            organizationId,
                            key -> new ArrayList<>()
                    )
                    .add(year);
        }

        Map<UUID, List<String>> technologiesMap = new HashMap<>();

        for (Object[] row : technologyResults) {
            UUID organizationId = (UUID) row[0];
            String technology = (String) row[1];

            technologiesMap
                    .computeIfAbsent(
                            organizationId,
                            key -> new ArrayList<>()
                    )
                    .add(technology);
        }


        List<OrganizationSummaryDTO> organizationDTOs =
                organizations.stream()
                        .map(organization -> {

                            UUID id = organization.getId();

                            return toSummaryDTO(
                                    organization,
                                    yearsMap.getOrDefault(id, List.of()),
                                    technologiesMap.getOrDefault(id, List.of())
                            );
                        })
                        .toList();

        return new OrganizationsResponseDTO(
                organizationPage.getNumber() + 1,
                organizationPage.getSize(),
                organizationDTOs,
                organizationPage.getTotalElements(),
                organizationPage.getTotalPages(),
                organizationPage.isFirst(),
                organizationPage.isLast()
        );
    }

    private OrganizationSummaryDTO toSummaryDTO(
            OrganizationEntity organization,
            List<Integer> years,
            List<String> technologies
    ) {
        UUID id = organization.getId();

        return new OrganizationSummaryDTO(
                organization.getId(),
                organization.getName(),
                organization.getImageUrl(),
                organization.getDescription(),
                organization.getGithubId(),
                organization.getImageBackgroundColor(),
                organization.isActiveOrg(),
                technologies,
                years

        );
    }

    public OrganizationDetailsDTO getOrganizationById(UUID id) {
        System.out.println("Organization ");
        OrganizationEntity organization = organizationRepository.findById(id).orElseThrow(() -> new OrganizationNotFoundException(
                "Organization not found: " + id
        ));
        System.out.println("Organization not found");
        List<Integer> years = organizationYearRepository.findYearsByOrganizationId(id);
        List<String> technologies = organizationTechnologyRepository.findTechnologyNameByOrganizationId(id);
        List<String> categories = organizationCategoryRepository.findCategoryNamesByOrganizationId(id);
        List<String> topics =organizationTopicRepository.findTopicNameByOrganizationId(id);
        OrganizationContactDTO contact = organizationContactRepository.findContactByOrganizationId(id).map(result -> new OrganizationContactDTO(
                result.getIrcChannel(),
                result.getContactEmail(),
                result.getMailingList(),
                result.getTwittterUrl(),
                result.getBlogUrl(),
                result.getFacebookUrl()
        )).orElse(null);
        return new OrganizationDetailsDTO(
                organization.getId(),
                organization.getName(),
                organization.getImageUrl(),
                organization.getImageBackgroundColor(),
                organization.getDescription(),
                organization.getUrl(),
                organization.getGithubId(),
                organization.isActiveOrg(),
                years,
                categories,
                topics,
                technologies,
                contact
        );
    }
}
