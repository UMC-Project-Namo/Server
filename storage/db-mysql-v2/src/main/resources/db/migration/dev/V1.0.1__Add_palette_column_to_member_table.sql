-- V1.0.1__Add_palette_column_to_member_table

-- 1. member table에 paltte_id 추가
ALTER TABLE member
    ADD COLUMN palette_id BIGINT,
    ADD CONSTRAINT fk_member_palette FOREIGN KEY (palette_id) REFERENCES palette (id);

-- 2. 기존 레코드에 대해 기본값 설정
UPDATE member
SET palette_id = 1
WHERE palette_id IS NULL;

-- 3. palette_id 컬럼을 NOT NULL로 변경
ALTER TABLE member
    MODIFY COLUMN palette_id BIGINT NOT NULL;
