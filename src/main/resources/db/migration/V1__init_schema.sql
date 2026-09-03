-- =========================================================
-- GSoC Hub - Initial Database Schema
-- =========================================================


-- ---------------------------------------------------------
-- UUID support
-- ---------------------------------------------------------

CREATE
EXTENSION IF NOT EXISTS pgcrypto;


-- =========================================================
-- 1. ORGANIZATIONS
-- =========================================================

CREATE TABLE organizations
(
    id                     UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    name                   VARCHAR(255) NOT NULL,
    image_url              TEXT,
    image_background_color VARCHAR(20),
    description            TEXT,
    url                    TEXT,
    github_id              VARCHAR(255),

    active_org             BOOLEAN      NOT NULL DEFAULT FALSE,

    created_at             TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at             TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- If GitHub ID exists:
-- name + github_id identifies an organization.
CREATE UNIQUE INDEX uq_organizations_name_github
    ON organizations (LOWER(name), LOWER(github_id)) WHERE github_id IS NOT NULL;


-- If GitHub ID doesn't exist:
-- name identifies the organization.
CREATE UNIQUE INDEX uq_organizations_name_without_github
    ON organizations (LOWER(name)) WHERE github_id IS NULL;


CREATE INDEX idx_organizations_github_id
    ON organizations (github_id);

CREATE INDEX idx_organizations_active
    ON organizations (active_org);


-- =========================================================
-- 2. ORGANIZATION CONTACT INFORMATION
-- =========================================================

CREATE TABLE organization_contacts
(
    organization_id UUID PRIMARY KEY,

    irc_channel     TEXT,
    contact_email   TEXT,
    mailing_list    TEXT,

    twitter_url     TEXT,
    blog_url        TEXT,
    facebook_url    TEXT,

    CONSTRAINT fk_contacts_organization
        FOREIGN KEY (organization_id)
            REFERENCES organizations (id)
            ON DELETE CASCADE
);


-- =========================================================
-- 3. ORGANIZATION YEARS
-- =========================================================
--
-- Examples:
-- AboutCode | 2017
-- AboutCode | 2019
-- AboutCode | 2025
-- AboutCode | 2030
--
-- No year is hard-coded into the schema.
-- =========================================================

CREATE TABLE organization_years
(
    organization_id UUID    NOT NULL,
    year            INTEGER NOT NULL,

    PRIMARY KEY (organization_id, year),

    CONSTRAINT fk_organization_years_organization
        FOREIGN KEY (organization_id)
            REFERENCES organizations (id)
            ON DELETE CASCADE
);


CREATE INDEX idx_organization_years_year
    ON organization_years (year);


-- =========================================================
-- 4. CATEGORIES
-- =========================================================

CREATE TABLE categories
(
    id   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL
);


CREATE UNIQUE INDEX uq_categories_name
    ON categories (LOWER(name));


CREATE TABLE organization_categories
(
    organization_id UUID NOT NULL,
    category_id     UUID NOT NULL,

    PRIMARY KEY (organization_id, category_id),

    CONSTRAINT fk_org_categories_organization
        FOREIGN KEY (organization_id)
            REFERENCES organizations (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_org_categories_category
        FOREIGN KEY (category_id)
            REFERENCES categories (id)
            ON DELETE CASCADE
);


CREATE INDEX idx_org_categories_category
    ON organization_categories (category_id);


-- =========================================================
-- 5. TOPICS
-- =========================================================

CREATE TABLE topics
(
    id   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL
);


CREATE UNIQUE INDEX uq_topics_name
    ON topics (LOWER(name));


CREATE TABLE organization_topics
(
    organization_id UUID NOT NULL,
    topic_id        UUID NOT NULL,

    PRIMARY KEY (organization_id, topic_id),

    CONSTRAINT fk_org_topics_organization
        FOREIGN KEY (organization_id)
            REFERENCES organizations (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_org_topics_topic
        FOREIGN KEY (topic_id)
            REFERENCES topics (id)
            ON DELETE CASCADE
);


CREATE INDEX idx_org_topics_topic
    ON organization_topics (topic_id);


-- =========================================================
-- 6. TECHNOLOGIES
-- =========================================================

CREATE TABLE technologies
(
    id   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL
);


CREATE UNIQUE INDEX uq_technologies_name
    ON technologies (LOWER(name));


CREATE TABLE organization_technologies
(
    organization_id UUID NOT NULL,
    technology_id   UUID NOT NULL,

    PRIMARY KEY (organization_id, technology_id),

    CONSTRAINT fk_org_technologies_organization
        FOREIGN KEY (organization_id)
            REFERENCES organizations (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_org_technologies_technology
        FOREIGN KEY (technology_id)
            REFERENCES technologies (id)
            ON DELETE CASCADE
);


CREATE INDEX idx_org_technologies_technology
    ON organization_technologies (technology_id);


-- =========================================================
-- 7. GSOC PROJECTS
-- =========================================================

CREATE TABLE gsoc_projects
(
    id                UUID PRIMARY KEY     DEFAULT gen_random_uuid(),

    organization_id   UUID        NOT NULL,
    year              INTEGER     NOT NULL,

    title             TEXT        NOT NULL,
    short_description TEXT,
    description       TEXT,

    student_name      VARCHAR(255),

    code_url          TEXT,
    proposal_id       VARCHAR(255),
    project_url       TEXT,

    created_at        TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_project_organization_year
        FOREIGN KEY (organization_id, year)
            REFERENCES organization_years (organization_id, year)
            ON DELETE CASCADE
);


CREATE INDEX idx_gsoc_projects_org_year
    ON gsoc_projects (organization_id, year);

CREATE INDEX idx_gsoc_projects_year
    ON gsoc_projects (year);


-- =========================================================
-- 8. GITHUB REPOSITORIES
-- =========================================================

CREATE TABLE repositories
(
    id              UUID PRIMARY KEY      DEFAULT gen_random_uuid(),

    organization_id UUID         NOT NULL,

    github_repo_id  BIGINT       NOT NULL,
    github_node_id  VARCHAR(255),

    name            VARCHAR(255) NOT NULL,
    full_name       VARCHAR(500),

    html_url        TEXT,
    description     TEXT,

    language        VARCHAR(255),

    stars           INTEGER      NOT NULL DEFAULT 0,
    forks           INTEGER      NOT NULL DEFAULT 0,
    open_issues     INTEGER      NOT NULL DEFAULT 0,

    license_key     VARCHAR(255),
    license_name    VARCHAR(255),
    license_spdx_id VARCHAR(255),
    license_url     TEXT,

    last_synced_at  TIMESTAMPTZ,

    CONSTRAINT fk_repository_organization
        FOREIGN KEY (organization_id)
            REFERENCES organizations (id)
            ON DELETE CASCADE,

    CONSTRAINT uq_repository_github_id
        UNIQUE (github_repo_id)
);


CREATE INDEX idx_repositories_organization
    ON repositories (organization_id);


-- =========================================================
-- 9. REPOSITORY TOPICS
-- =========================================================
--
-- GitHub repository topics are different from the
-- organization's GSoC topics, so keep them separate.
-- =========================================================

CREATE TABLE repository_topics
(
    repository_id UUID         NOT NULL,
    topic         VARCHAR(255) NOT NULL,

    PRIMARY KEY (repository_id, topic),

    CONSTRAINT fk_repository_topics_repository
        FOREIGN KEY (repository_id)
            REFERENCES repositories (id)
            ON DELETE CASCADE
);


-- =========================================================
-- 10. GITHUB CONTRIBUTORS
-- =========================================================
--
-- Contributor itself is stored once.
-- Contribution count belongs to the relationship between
-- contributor and organization.
-- =========================================================

CREATE TABLE contributors
(
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    github_user_id BIGINT       NOT NULL,
    github_node_id VARCHAR(255),

    login          VARCHAR(255) NOT NULL,

    avatar_url     TEXT,
    html_url       TEXT,

    CONSTRAINT uq_contributor_github_id
        UNIQUE (github_user_id)
);


CREATE INDEX idx_contributors_login
    ON contributors (login);


CREATE TABLE organization_contributors
(
    organization_id UUID    NOT NULL,
    contributor_id  UUID    NOT NULL,

    contributions   INTEGER NOT NULL DEFAULT 0,

    PRIMARY KEY (organization_id, contributor_id),

    CONSTRAINT fk_org_contributors_organization
        FOREIGN KEY (organization_id)
            REFERENCES organizations (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_org_contributors_contributor
        FOREIGN KEY (contributor_id)
            REFERENCES contributors (id)
            ON DELETE CASCADE
);


CREATE INDEX idx_org_contributors_contributor
    ON organization_contributors (contributor_id);


-- =========================================================
-- 11. GITHUB ACTIVITY / COMMIT STATISTICS
-- =========================================================
--
-- Keeps the underlying activity data used to determine
-- active_org.
--
-- Multiple rows can be stored over time so we retain
-- history instead of overwriting the previous result.
-- =========================================================

CREATE TABLE organization_github_stats
(
    id              UUID PRIMARY KEY     DEFAULT gen_random_uuid(),

    organization_id UUID        NOT NULL,

    commit_count    BIGINT      NOT NULL DEFAULT 0,

    calculated_at   TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_github_stats_organization
        FOREIGN KEY (organization_id)
            REFERENCES organizations (id)
            ON DELETE CASCADE
);


CREATE INDEX idx_github_stats_organization
    ON organization_github_stats (organization_id);

CREATE INDEX idx_github_stats_calculated_at
    ON organization_github_stats (calculated_at);


-- =========================================================
-- 12. DATA IMPORT / SYNC HISTORY
-- =========================================================
--
-- Useful when GitHub Actions runs the compiler later.
--
-- Example:
-- source = GSOC_GITHUB
-- status = SUCCESS
-- records_processed = 1500
-- =========================================================

CREATE TABLE data_sync_runs
(
    id                UUID PRIMARY KEY      DEFAULT gen_random_uuid(),

    source            VARCHAR(100) NOT NULL,

    status            VARCHAR(20)  NOT NULL,

    started_at        TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    finished_at       TIMESTAMPTZ,

    records_processed INTEGER      NOT NULL DEFAULT 0,

    error_message     TEXT,

    CONSTRAINT chk_data_sync_status
        CHECK (status IN ('RUNNING', 'SUCCESS', 'FAILED'))
);
