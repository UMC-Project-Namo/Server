-- V1.0.1__Add_palette_column_to_member_table

-- Add palette_id column for table `member`
ALTER TABLE member
    ADD COLUMN palette_id BIGINT,
    ADD CONSTRAINT fk_member_palette FOREIGN KEY (palette_id) REFERENCES palette (id);
