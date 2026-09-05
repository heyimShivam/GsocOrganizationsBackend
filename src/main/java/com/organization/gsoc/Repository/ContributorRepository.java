package com.organization.gsoc.Repository;

import com.organization.gsoc.Entity.ContributorEntity;
import com.organization.gsoc.Repository.Projection.ContributorProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ContributorRepository extends JpaRepository<ContributorEntity, UUID> {

    @Query(
            value = """
                    SELECT
                        c.id AS id,
                        c.github_user_id AS githubUserId,
                        c.github_node_id AS githubNodeId,
                        c.login AS login,
                        c.avatar_url AS avatarUrl,
                        c.html_url AS htmlUrl,
                        oc.contributions AS contributions
                    FROM organization_contributors oc
                    JOIN contributors c
                        ON c.id = oc.contributor_id
                    WHERE oc.organization_id = :organizationId
                    """,

            countQuery = """
                    SELECT COUNT(*)
                    FROM organization_contributors
                    WHERE organization_id = :organizationId
                    """,

            nativeQuery = true
    )
    Page<ContributorProjection> findContributorsByOrganizationId(
            @Param("organizationId") UUID organizationId,
            Pageable pageable
    );
}