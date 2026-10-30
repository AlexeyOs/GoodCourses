-- Диагностика выполняется до добавления ограничения. При наличии некорректных
-- значений миграция останавливается и не изменяет пользовательские данные.
BEGIN;

SELECT id, rating
FROM feedback
WHERE rating IS NOT NULL
  AND rating NOT BETWEEN 1 AND 5;

DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM feedback
        WHERE rating IS NOT NULL
          AND rating NOT BETWEEN 1 AND 5
    ) THEN
        RAISE EXCEPTION 'feedback содержит оценки вне диапазона 1..5; исправьте данные перед миграцией';
    END IF;
END
$$;

ALTER TABLE feedback
    ADD CONSTRAINT chk_feedback_rating
    CHECK (rating IS NULL OR rating BETWEEN 1 AND 5);

COMMIT;
