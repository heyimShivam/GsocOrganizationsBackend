package com.organization.gsoc.Repository;

import com.organization.gsoc.Entity.ContactMessageEntity;
import com.organization.gsoc.Enums.ContactMessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ContactMessageRepository
        extends JpaRepository<ContactMessageEntity, UUID> {

    List<ContactMessageEntity> findByStatusOrderByCreatedAtDesc(
            ContactMessageStatus status
    );

    List<ContactMessageEntity> findAllByOrderByCreatedAtDesc();
}