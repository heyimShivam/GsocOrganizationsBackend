package com.organization.gsoc.Entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "organizations")
public class OrganizationEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "image_background_color")
    private String imageBackgroundColor;

    @Column (columnDefinition = "Text")
    private String description;

    private String url;

    @Column(name = "github_id")
    private String githubId;

    @Column(name = "active_org", nullable = false)
    private boolean activeOrg;

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageBackgroundColor() {
        return imageBackgroundColor;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getGithubId() {
        return githubId;
    }

    public boolean isActiveOrg() {
        return activeOrg;
    }
}
