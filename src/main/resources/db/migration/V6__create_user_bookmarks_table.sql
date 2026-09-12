CREATE TABLE user_bookmarks (
                                id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                                user_id UUID NOT NULL,
                                organization_id UUID NOT NULL,

                                created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                CONSTRAINT fk_user_bookmarks_user
                                    FOREIGN KEY (user_id)
                                        REFERENCES users(id)
                                        ON DELETE CASCADE,

                                CONSTRAINT fk_user_bookmarks_organization
                                    FOREIGN KEY (organization_id)
                                        REFERENCES organizations(id)
                                        ON DELETE CASCADE,

                                CONSTRAINT uk_user_bookmarks_user_organization
                                    UNIQUE (user_id, organization_id)
);

CREATE INDEX idx_user_bookmarks_user
    ON user_bookmarks(user_id);

CREATE INDEX idx_user_bookmarks_organization
    ON user_bookmarks(organization_id);