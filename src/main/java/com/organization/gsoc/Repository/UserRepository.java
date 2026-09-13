package com.organization.gsoc.Repository;

import com.organization.gsoc.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByGithubUsername(String githubUsername);

    boolean existsByEmail(String email);

    boolean existsByGithubUsername(String githubUsername);

    boolean existsByGithubUsernameAndIdNot(
            String githubUsername,
            UUID id
    );

}
