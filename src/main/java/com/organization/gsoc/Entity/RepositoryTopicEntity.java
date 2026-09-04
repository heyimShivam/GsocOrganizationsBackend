package com.organization.gsoc.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "repository_topics")
public class RepositoryTopicEntity {

    @EmbeddedId
    private RepositoryTopicId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("repositoryId")
    @JoinColumn(name = "repository_id")
    private RepositoryEntity repository;

    public RepositoryTopicId getId() {
        return id;
    }

    public RepositoryEntity getRepository() {
        return repository;
    }

    public String getTopic() {
        return id.getTopic();
    }
}