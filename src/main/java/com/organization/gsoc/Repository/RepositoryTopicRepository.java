package com.organization.gsoc.Repository;

import com.organization.gsoc.Entity.RepositoryTopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface RepositoryTopicRepository
        extends JpaRepository<RepositoryTopicEntity, UUID> {

    @Query(
            value = """
                    SELECT topic
                    FROM repository_topics
                    WHERE repository_id = :repositoryId
                      AND topic IS NOT NULL
                    ORDER BY topic
                    """,
            nativeQuery = true
    )
    List<String> findTopicNamesByRepositoryId(
            @Param("repositoryId") UUID repositoryId
    );
}