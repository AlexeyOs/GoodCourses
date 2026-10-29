BEGIN;

DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM feedback
        WHERE description IS NULL OR start_date IS NULL
    ) THEN
        RAISE EXCEPTION 'feedback contains NULL description or start_date values';
    END IF;

    IF EXISTS (
        SELECT 1
        FROM feedback
        GROUP BY course_id, profile_id
        HAVING COUNT(*) > 1
    ) THEN
        RAISE EXCEPTION 'feedback contains duplicate course/profile pairs';
    END IF;
END
$$;

ALTER TABLE feedback
    ALTER COLUMN description SET NOT NULL,
    ALTER COLUMN start_date SET NOT NULL;

ALTER TABLE feedback
    ADD CONSTRAINT uq_feedback_course_profile
    UNIQUE (course_id, profile_id);

COMMIT;
