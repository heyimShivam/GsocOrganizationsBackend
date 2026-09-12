CREATE TABLE contact_messages (
                                  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                                  name VARCHAR(255) NOT NULL,
                                  email VARCHAR(255) NOT NULL,
                                  subject VARCHAR(255) NOT NULL,
                                  message TEXT NOT NULL,

                                  status VARCHAR(30) NOT NULL DEFAULT 'NEW',

                                  admin_reply TEXT,
                                  replied_at TIMESTAMPTZ,

                                  created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                  CONSTRAINT chk_contact_message_status
                                      CHECK (status IN ('NEW', 'IN_PROGRESS', 'RESOLVED'))
);

CREATE INDEX idx_contact_messages_status
    ON contact_messages(status);

CREATE INDEX idx_contact_messages_created_at
    ON contact_messages(created_at);