UPDATE profile
SET large_photo = NULL
WHERE large_photo ~ '^https?://';

UPDATE profile
SET small_photo = NULL
WHERE small_photo ~ '^https?://';
