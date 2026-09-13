CREATE TABLE chat_messages (
                               id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                               user_id UUID NOT NULL,

                               message TEXT NOT NULL,

                               created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                               CONSTRAINT fk_chat_messages_user
                                   FOREIGN KEY (user_id)
                                       REFERENCES users(id)
                                       ON DELETE CASCADE
);

CREATE INDEX idx_chat_messages_created_at
    ON chat_messages(created_at);

CREATE INDEX idx_chat_messages_user_id
    ON chat_messages(user_id);