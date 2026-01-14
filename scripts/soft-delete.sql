-- Soft delete migration for organizations, users, courses
ALTER TABLE organizations ADD COLUMN active BOOLEAN NOT NULL DEFAULT TRUE;
ALTER TABLE users ADD COLUMN active BOOLEAN NOT NULL DEFAULT TRUE;
ALTER TABLE courses ADD COLUMN active BOOLEAN NOT NULL DEFAULT TRUE;

UPDATE organizations SET active = TRUE WHERE active IS NULL;
UPDATE users SET active = TRUE WHERE active IS NULL;
UPDATE courses SET active = TRUE WHERE active IS NULL;
