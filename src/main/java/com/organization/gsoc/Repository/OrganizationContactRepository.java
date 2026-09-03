package com.organization.gsoc.Repository;

import com.organization.gsoc.Entity.OrganizationEntity;
import com.organization.gsoc.Repository.Projection.OrganizationContactProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrganizationContactRepository extends JpaRepository<OrganizationEntity, UUID>  {
    @Query(
            value = """
                    SELECT irc_channel AS ircChannel, contact_email AS contactEmail, mailing_list AS mailingList, twitter_url AS twitterUrl, blog_url AS blogUrl, facebook_url as facebookUrl  FROM organization_contacts
                                 where organization_id = :organizationId
                    """,
            nativeQuery = true
    )

    Optional<OrganizationContactProjection> findContactByOrganizationId (
            @Param("organizationId") UUID organizationId
    );
}
