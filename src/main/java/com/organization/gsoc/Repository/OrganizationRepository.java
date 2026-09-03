package com.organization.gsoc.Repository;

import com.organization.gsoc.Entity.OrganizationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

import com.organization.gsoc.Entity.OrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrganizationRepository
        extends JpaRepository<OrganizationEntity, UUID>,
        OrganizationRepositoryCustom {
}