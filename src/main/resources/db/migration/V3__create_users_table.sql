CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       password VARCHAR(255),
                       github_username VARCHAR(255),
                       role VARCHAR(20) NOT NULL DEFAULT 'USER',
                       email_verified BOOLEAN NOT NULL DEFAULT FALSE,
                       created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                       CONSTRAINT uk_users_email UNIQUE (email),

                       CONSTRAINT chk_users_role
                           CHECK (role IN ('USER', 'ADMIN'))
);