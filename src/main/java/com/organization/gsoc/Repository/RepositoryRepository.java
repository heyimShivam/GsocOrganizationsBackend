package com.organization.gsoc.Repository;

import com.organization.gsoc.Entity.RepositoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface RepositoryRepository
        extends JpaRepository<RepositoryEntity, UUID> {

    @Query(
            value = """
                    SELECT r.*
                    FROM repositories r
                    JOIN organization_repositories org_repo
                        ON r.id = org_repo.repository_id
                    WHERE org_repo.organization_id = :organizationId
                    """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM organization_repositories
                    WHERE organization_id = :organizationId
                    """,
            nativeQuery = true
    )
    Page<RepositoryEntity> findRepositoriesByOrganizationId(
            @Param("organizationId") UUID organizationId,
            Pageable pageable
    );


    @Query(
            value = """
                    SELECT r.*
                    FROM repositories r
                    JOIN organization_repositories org_repo
                        ON r.id = org_repo.repository_id
                    WHERE org_repo.organization_id = :organizationId
                    AND LOWER(r.name) LIKE LOWER(
                        CONCAT('%', :repoName, '%')
                    )
                    """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM repositories r
                    JOIN organization_repositories org_repo
                        ON r.id = org_repo.repository_id
                    WHERE org_repo.organization_id = :organizationId
                    AND LOWER(r.name) LIKE LOWER(
                        CONCAT('%', :repoName, '%')
                    )
                    """,
            nativeQuery = true
    )
    Page<RepositoryEntity> findRepositoriesByOrganizationId(
            @Param("organizationId") UUID organizationId,
            @Param("repoName") String repoName,
            Pageable pageable
    );
}