package com.organization.gsoc.Entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "contributors")
public class ContributorEntity {

    @Id
    private UUID id;

    @Column(name = "github_user_id")
    private Long githubUserId;

    @Column(name = "github_node_id")
    private String githubNodeId;

    private String login;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "html_url")
    private String htmlUrl;

    public UUID getId() {
        return id;
    }

    public Long getGithubUserId() {
        return githubUserId;
    }

    public String getGithubNodeId() {
        return githubNodeId;
    }

    public String getLogin() {
        return login;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }
}