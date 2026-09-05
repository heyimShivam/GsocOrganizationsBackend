package com.organization.gsoc.Repository;

import com.organization.gsoc.Entity.OrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OrganizationTechnologyRepository extends JpaRepository<OrganizationEntity, UUID>  {
    @Query(
            value = """
             SELECT t.name FROM technologies t JOIN organization_technologies ot ON ot.technology_id = t.id
                          where ot.organization_id = :organizationId
                          ORDER BY t.name ASC
             """,
            nativeQuery = true
    )
    List<String> findTechnologyNameByOrganizationId (
            @Param("organizationId") UUID organizationId
    );

    @Query(
            value = """
                    SELECT ot.organization_id, t.name
                    FROM technologies t
                    JOIN organization_technologies ot
                        ON ot.technology_id = t.id
                    WHERE ot.organization_id IN (:organizationIds)
                    ORDER BY t.name ASC
                    """,
            nativeQuery = true
    )
    List<Object[]> findTechnologyNamesByOrganizationIds(
            @Param("organizationIds") List<UUID> organizationIds
    );

    @Query(
            value = """
                SELECT DISTINCT t.name
                FROM technologies t
                JOIN organization_technologies ot
                    ON ot.technology_id = t.id
                ORDER BY t.name ASC
                """,
            nativeQuery = true
    )
    List<String> findAllTechnologyNames();
}
