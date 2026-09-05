package com.organization.gsoc.Repository;

import com.organization.gsoc.Entity.GsocProjectEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface GsocProjectRepository
        extends JpaRepository<GsocProjectEntity, UUID> {

    Page<GsocProjectEntity> findByOrganizationId(
            UUID organizationId,
            Pageable pageable
    );

    Page<GsocProjectEntity> findByOrganizationIdAndYear(
            UUID organizationId,
            int year,
            Pageable pageable
    );

    @Query("""
            SELECT p.year, COUNT(p)
            FROM GsocProjectEntity p
            WHERE p.organizationId = :organizationId
            GROUP BY p.year
            ORDER BY p.year DESC
            """)
    List<Object[]> findProjectCountByYear(
            @Param("organizationId") UUID organizationId
    );
}