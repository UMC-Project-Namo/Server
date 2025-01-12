-- V1.1.9__Drop_palette_column_from_participant_table

SET foreign_key_checks = 0;
ALTER TABLE participant DROP FOREIGN KEY fk_participant_palette;
ALTER TABLE participant DROP COLUMN palette_id;
SET foreign_key_checks = 1;

