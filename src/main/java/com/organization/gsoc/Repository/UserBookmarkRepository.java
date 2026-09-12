package com.organization.gsoc.Repository;

import com.organization.gsoc.Entity.UserBookmarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserBookmarkRepository
        extends JpaRepository<UserBookmarkEntity, UUID> {

    List<UserBookmarkEntity> findByUserId(UUID userId);

    boolean existsByUserIdAndOrganizationId(
            UUID userId,
            UUID organizationId
    );

    void deleteByUserIdAndOrganizationId(
            UUID userId,
            UUID organizationId
    );
}