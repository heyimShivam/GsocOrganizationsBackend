package com.organization.gsoc.Repository;

import com.organization.gsoc.Entity.OrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OrganizationTopicRepository extends JpaRepository<OrganizationEntity, UUID>  {
    @Query(
            value = """
             SELECT t.name from topics t JOIN organization_topics ot ON ot.topic_id = t.id
                          where ot.organization_id = :organizationId
                          ORDER BY t.name ASC
             """,
            nativeQuery = true
    )
    List<String> findTopicNameByOrganizationId(
            @Param("organizationId") UUID organizationId
    );
}
