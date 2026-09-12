package com.organization.gsoc.DTO.Auth;

import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.UUID;

public class AuthUserResponse
        extends RepresentationModel<AuthUserResponse> {

    private UUID id;
    private String name;
    private String email;
    private String role;
    private String description;
    private String quote;
    private String githubUsername;
    private List<UUID> bookmarkedOrganizationIds;

    public AuthUserResponse() {
    }

    public AuthUserResponse(
            UUID id,
            String name,
            String email,
            String role,
            String description,
            String quote,
            String githubUsername,
            List<UUID> bookmarkedOrganizationIds
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.description = description;
        this.quote = quote;
        this.githubUsername = githubUsername;
        this.bookmarkedOrganizationIds = bookmarkedOrganizationIds;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getGithubUsername() {
        return githubUsername;
    }

    public void setGithubUsername(String githubUsername) {
        this.githubUsername = githubUsername;
    }

    public List<UUID> getBookmarkedOrganizationIds() {
        return bookmarkedOrganizationIds;
    }

    public void setBookmarkedOrganizationIds(List<UUID> bookmarkedOrganizationIds) {
        this.bookmarkedOrganizationIds = bookmarkedOrganizationIds;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}