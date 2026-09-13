CREATE TABLE chat_channels (
                               id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                               name VARCHAR(50) NOT NULL,

                               description VARCHAR(255) NOT NULL,

                               created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                               CONSTRAINT uk_chat_channels_name UNIQUE (name)
);

INSERT INTO chat_channels (name, description)
VALUES
    (
        'GENERAL',
        'Open discussions and GSoC community conversations'
    ),
    (
        'HELP',
        'Ask questions and help other contributors'
    ),
    (
        'OFF_TOPIC',
        'Casual conversations outside GSoC'
    );

ALTER TABLE chat_messages
    ADD COLUMN channel_id UUID;

UPDATE chat_messages
SET channel_id = (
    SELECT id
    FROM chat_channels
    WHERE name = 'GENERAL'
)
WHERE channel_id IS NULL;

ALTER TABLE chat_messages
    ALTER COLUMN channel_id SET NOT NULL;

ALTER TABLE chat_messages
    ADD CONSTRAINT fk_chat_messages_channel
        FOREIGN KEY (channel_id)
            REFERENCES chat_channels(id)
            ON DELETE CASCADE;

CREATE INDEX idx_chat_messages_channel_created
    ON chat_messages(channel_id, created_at);