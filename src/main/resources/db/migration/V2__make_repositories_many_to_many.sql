CREATE TABLE organization_repositories
(
    organization_id UUID NOT NULL,
    repository_id   UUID NOT NULL,

    PRIMARY KEY (organization_id, repository_id),

    CONSTRAINT fk_org_repositories_organization
        FOREIGN KEY (organization_id)
            REFERENCES organizations (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_org_repositories_repository
        FOREIGN KEY (repository_id)
            REFERENCES repositories (id)
            ON DELETE CASCADE
);

CREATE INDEX idx_org_repositories_repository
    ON organization_repositories (repository_id);

-- Preserve any data if repositories were already imported before this migration.
INSERT INTO organization_repositories (organization_id, repository_id)
SELECT organization_id, id
FROM repositories
WHERE organization_id IS NOT NULL ON CONFLICT DO NOTHING;

DROP INDEX IF EXISTS idx_repositories_organization;

ALTER TABLE repositories
DROP
CONSTRAINT IF EXISTS fk_repository_organization;

ALTER TABLE repositories
DROP
COLUMN organization_id;
