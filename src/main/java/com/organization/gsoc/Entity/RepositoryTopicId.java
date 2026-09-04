package com.organization.gsoc.Entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class RepositoryTopicId implements Serializable {

    private UUID repositoryId;

    private String topic;

    public RepositoryTopicId() {
    }

    public UUID getRepositoryId() {
        return repositoryId;
    }

    public String getTopic() {
        return topic;
    }
}