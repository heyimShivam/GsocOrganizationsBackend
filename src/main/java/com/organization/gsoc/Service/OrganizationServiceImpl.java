package com.organization.gsoc.Service;

import com.organization.gsoc.DTO.OrganizationContactDTO;
import com.organization.gsoc.DTO.OrganizationDetailsDTO;
import com.organization.gsoc.DTO.OrganizationSummaryDTO;
import com.organization.gsoc.DTO.OrganizationsResponseDTO;
import com.organization.gsoc.Entity.OrganizationEntity;
import com.organization.gsoc.Exception.NoPageException;
import com.organization.gsoc.Exception.OrganicationNotFoundException;
import com.organization.gsoc.Repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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

    public OrganizationsResponseDTO getOrganizations(String search, int page, int size) {
        long totalRecords = organizationRepository.count();
        int totalPages = (int) Math.ceil((double) totalRecords / size);

        if (page < 1 || page > totalPages) {
            throw new NoPageException(
                    "Page " + page + " does not exist. Total pages available: " + totalPages
            );
        }

        PageRequest pageable = PageRequest.of(page - 1, size, Sort.by("name").ascending());

        Page<OrganizationEntity> organizationPage;

        if(search == null || search.isBlank()) {
            organizationPage = organizationRepository.findAll(pageable);
        } else {
            organizationPage = organizationRepository.findByNameContainingIgnoreCase(search.trim(), pageable);
        }

        List<OrganizationSummaryDTO> organizations = organizationPage.getContent().stream().map(this::toSummaryDTO).toList();

        return new OrganizationsResponseDTO(
                organizationPage.getNumber() + 1,
                organizationPage.getSize(),
                organizations,
                organizationPage.getTotalElements(),
                organizationPage.getTotalPages(),
                organizationPage.isFirst(),
                organizationPage.isLast()
        );
    }

    private OrganizationSummaryDTO toSummaryDTO(
            OrganizationEntity organization
    ) {
        return new OrganizationSummaryDTO(
                organization.getId(),
                organization.getName(),
                organization.getImageUrl(),
                organization.getDescription(),
                organization.getGithubId(),
                organization.isActiveOrg()
        );
    }

    public OrganizationDetailsDTO getOrganizationById(UUID id) {
        System.out.println("Organization ");
        OrganizationEntity organization = organizationRepository.findById(id).orElseThrow(() -> new OrganicationNotFoundException(
                "Organization not found: " + id
        ));
        System.out.println("Organization not found");
        List<Integer> years = organizationYearRepository.findYearsByOrganizationId(id);
        List<String> categories = organizationCategoryRepository.findCategoryNamesByOrganizationId(id);
        List<String> topics =organizationTopicRepository.findTopicNameByOrganizationId(id);
        List<String> technologies = organizationTechnologyRepository.findTechnologyNameByOrganizationId(id);
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
