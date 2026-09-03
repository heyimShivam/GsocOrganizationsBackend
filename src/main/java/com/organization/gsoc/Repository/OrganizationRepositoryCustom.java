package com.organization.gsoc.Repository;

import com.organization.gsoc.DTO.OrganizationFilterDTO;
import com.organization.gsoc.Entity.OrganizationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrganizationRepositoryCustom {

    Page<OrganizationEntity> searchOrganizations(
            OrganizationFilterDTO filter,
            Pageable pageable
    );
}