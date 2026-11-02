ALTER TABLE profile
    ADD COLUMN password_state VARCHAR(20);

UPDATE profile
SET password_state = CASE
        WHEN password ~ '^\$2[aby]\$[0-9]{2}\$[./A-Za-z0-9]{53}$' THEN 'ACTIVE'
        ELSE 'RESET_REQUIRED'
    END;

UPDATE profile
SET password = NULL
WHERE password_state = 'RESET_REQUIRED';

ALTER TABLE profile
    ALTER COLUMN password DROP NOT NULL,
    ALTER COLUMN password_state SET NOT NULL;

ALTER TABLE profile
    ADD CONSTRAINT chk_profile_password_state
    CHECK (
        (password_state = 'ACTIVE'
            AND password ~ '^\$2[aby]\$[0-9]{2}\$[./A-Za-z0-9]{53}$')
        OR
        (password_state IN ('RESET_REQUIRED', 'EXTERNAL_ONLY')
            AND password IS NULL)
    );
