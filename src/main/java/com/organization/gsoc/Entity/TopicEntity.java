package com.organization.gsoc.Entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "topics")
public class TopicEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}