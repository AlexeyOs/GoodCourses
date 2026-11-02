DELETE FROM profile_restore;

ALTER TABLE profile_restore
    DROP COLUMN token,
    ADD COLUMN token_hash VARCHAR(64) NOT NULL,
    ADD COLUMN expires_at TIMESTAMP WITHOUT TIME ZONE NOT NULL;

ALTER TABLE profile_restore
    ADD CONSTRAINT uq_profile_restore_token_hash UNIQUE (token_hash),
    ADD CONSTRAINT chk_profile_restore_token_hash
        CHECK (token_hash ~ '^[0-9a-f]{64}$');
