package com.organization.gsoc.Repository;

import com.organization.gsoc.Entity.OrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OrganizationYearRepository extends JpaRepository<OrganizationEntity, UUID> {
    @Query(
            value = """
             SELECT year from organization_years
                          where organization_id = :organizationId
                          ORDER BY year ASC
             """,
            nativeQuery = true
    )
    List<Integer> findYearsByOrganizationId(
            @Param("organizationId") UUID organizationId
    );
}
