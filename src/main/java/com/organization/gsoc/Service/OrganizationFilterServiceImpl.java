package com.organization.gsoc.Service;

import com.organization.gsoc.DTO.OrganizationFiltersResponseDTO;
import com.organization.gsoc.Repository.OrganizationCategoryRepository;
import com.organization.gsoc.Repository.OrganizationTechnologyRepository;
import com.organization.gsoc.Repository.OrganizationTopicRepository;
import com.organization.gsoc.Repository.OrganizationYearRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationFilterServiceImpl
        implements OrganizationFilterService {

    private final OrganizationCategoryRepository organizationCategoryRepository;
    private final OrganizationTopicRepository organizationTopicRepository;
    private final OrganizationTechnologyRepository organizationTechnologyRepository;
    private final OrganizationYearRepository organizationYearRepository;

    public OrganizationFilterServiceImpl(
            OrganizationCategoryRepository organizationCategoryRepository,
            OrganizationTopicRepository organizationTopicRepository,
            OrganizationTechnologyRepository organizationTechnologyRepository,
            OrganizationYearRepository organizationYearRepository
    ) {
        this.organizationCategoryRepository =
                organizationCategoryRepository;

        this.organizationTopicRepository =
                organizationTopicRepository;

        this.organizationTechnologyRepository =
                organizationTechnologyRepository;

        this.organizationYearRepository =
                organizationYearRepository;
    }

    @Override
    public OrganizationFiltersResponseDTO getFilters() {

        List<String> categories =
                organizationCategoryRepository
                        .findAllCategoryNames();

        List<String> topics =
                organizationTopicRepository
                        .findAllTopicNames();

        List<String> technologies =
                organizationTechnologyRepository
                        .findAllTechnologyNames();

        List<Integer> years =
                organizationYearRepository
                        .findAllYears();

        return new OrganizationFiltersResponseDTO(
                categories,
                topics,
                technologies,
                years
        );
    }
}