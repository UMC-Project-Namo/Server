-- V1.0.5__Modify_palette_column_from_member_table

-- palette 컬럼을 NULL 허용으로 변경
ALTER TABLE member
    MODIFY COLUMN palette_id BIGINT NULL;