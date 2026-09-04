package com.organization.gsoc.Repository;

import com.organization.gsoc.Entity.GsocProjectEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GsocProjectRepository
        extends JpaRepository<GsocProjectEntity, UUID> {

    Page<GsocProjectEntity> findByOrganizationId(
            UUID organizationId,
            Pageable pageable
    );

    Page<GsocProjectEntity>
    findByOrganizationIdAndYear(
            UUID organizationId,
            int year,
            Pageable pageable
    );
}