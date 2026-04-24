-- liquibase formatted sql

-- changeset author:1
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM pg_indexes WHERE indexname = 'idx_student_name'
CREATE INDEX IF NOT EXISTS idx_student_name ON student (name);

-- changeset author:2
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM pg_indexes WHERE indexname = 'idx_faculty_name_color'
CREATE INDEX IF NOT EXISTS idx_faculty_name_color ON faculty (name, color);