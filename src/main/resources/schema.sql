-- 0. Global Configuration (External to transaction)
-- DO $$
-- BEGIN
--     IF EXISTS (SELECT 1 FROM pg_roles WHERE rolname = 'vishal') THEN
--         ALTER ROLE vishal SET TIMEZONE TO 'UTC';
--     END IF;
-- END $$^;

-- ALTER DATABASE "rexbase-new" SET TIMEZONE TO 'UTC'^;

-- Wrapped in a transaction for safety
BEGIN^;

COMMIT^;

