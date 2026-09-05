package com.organization.gsoc.Repository;

import com.organization.gsoc.Entity.OrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OrganizationCategoryRepository extends JpaRepository<OrganizationEntity, UUID> {
    @Query(
            value = """
            SELECT c.name
            FROM categories c
            JOIN organization_categories oc
                ON oc.category_id = c.id
            WHERE oc.organization_id = :organizationId
            ORDER BY c.name ASC
             """,
            nativeQuery = true
    )
    List<String> findCategoryNamesByOrganizationId(
            @Param("organizationId") UUID organizationId
    );

    @Query(
            value = """
                    SELECT oc.organization_id, c.name
                    FROM categories c
                    JOIN organization_categories oc
                        ON oc.category_id = c.id
                    WHERE oc.organization_id IN (:organizationIds)
                    ORDER BY c.name ASC
                    """,
            nativeQuery = true
    )
    List<Object[]> findCategoryNamesByOrganizationIds(
            @Param("organizationIds") List<UUID> organizationIds
    );

    @Query(
            value = """
                SELECT DISTINCT c.name
                FROM categories c
                JOIN organization_categories oc
                    ON oc.category_id = c.id
                ORDER BY c.name ASC
                """,
            nativeQuery = true
    )
    List<String> findAllCategoryNames();
}
