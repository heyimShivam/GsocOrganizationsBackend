CREATE TABLE email_verification_tokens (
                                           id UUID PRIMARY KEY,
                                           user_id UUID NOT NULL,
                                           token VARCHAR(255) NOT NULL,
                                           expires_at TIMESTAMPTZ NOT NULL,
                                           used BOOLEAN NOT NULL DEFAULT FALSE,
                                           created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                           CONSTRAINT uk_email_verification_token
                                               UNIQUE (token),

                                           CONSTRAINT fk_email_verification_user
                                               FOREIGN KEY (user_id)
                                                   REFERENCES users(id)
                                                   ON DELETE CASCADE
);