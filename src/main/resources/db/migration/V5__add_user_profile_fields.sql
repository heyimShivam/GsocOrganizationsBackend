ALTER TABLE users
    ADD COLUMN description TEXT NOT NULL DEFAULT 'Exploring open source';

ALTER TABLE users
    ADD COLUMN quote TEXT NOT NULL DEFAULT 'Open source today, a brighter tomorrow';