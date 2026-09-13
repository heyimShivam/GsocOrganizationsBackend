ALTER TABLE users
    ADD CONSTRAINT uk_users_github_username
        UNIQUE (github_username);