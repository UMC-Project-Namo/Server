-- V1.1.9__Drop_palette_column_from_participant_table

SET foreign_key_checks = 0;
ALTER TABLE participant DROP FOREIGN KEY FK7q1eb1aao98hr1nsa85agu6x5;
ALTER TABLE participant DROP COLUMN palette_id;
SET foreign_key_checks = 1;

