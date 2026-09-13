package com.organization.gsoc.Repository;

import com.organization.gsoc.Entity.ChatChannelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ChatChannelRepository
        extends JpaRepository<ChatChannelEntity, UUID> {

    Optional<ChatChannelEntity> findByName(String name);
}