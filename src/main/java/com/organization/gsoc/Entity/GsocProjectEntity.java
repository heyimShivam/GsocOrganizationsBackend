package com.organization.gsoc.Entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "gsoc_projects")
public class GsocProjectEntity {

    @Id
    private UUID id;

    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    private int year;

    @Column(columnDefinition = "TEXT")
    private String title;

    @Column(name = "short_description", columnDefinition = "TEXT")
    private String shortDescription;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "student_name")
    private String studentName;

    @Column(name = "code_url", columnDefinition = "TEXT")
    private String codeUrl;

    @Column(name = "proposal_id")
    private String proposalId;

    @Column(name = "project_url", columnDefinition = "TEXT")
    private String projectUrl;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;


    public UUID getId() {
        return id;
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public int getYear() {
        return year;
    }

    public String getTitle() {
        return title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getCodeUrl() {
        return codeUrl;
    }

    public String getProposalId() {
        return proposalId;
    }

    public String getProjectUrl() {
        return projectUrl;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}