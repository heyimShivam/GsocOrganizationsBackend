package com.organization.gsoc.Repository.Projection;

import java.util.UUID;

public interface ContributorProjection {

    UUID getId();

    Long getGithubUserId();

    String getGithubNodeId();

    String getLogin();

    String getAvatarUrl();

    String getHtmlUrl();

    Integer getContributions();
}