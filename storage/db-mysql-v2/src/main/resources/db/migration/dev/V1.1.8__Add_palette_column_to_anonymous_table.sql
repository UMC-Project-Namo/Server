-- V1.1.8__Add_palette_column_to_anonymous_table

-- 1. anonymous에 palette_id 추가
ALTER TABLE anonymous
    ADD COLUMN palette_id BIGINT,
    ADD CONSTRAINT fk_anonymous_palette FOREIGN KEY (palette_id) REFERENCES palette (id);

-- 2. participant의 palette_id를 anonymous의 palette_id로 복사
UPDATE anonymous a
    JOIN participant p ON a.id = p.anonymous_id
    SET a.palette_id = p.palette_id
WHERE p.palette_id IS NOT NULL;

ALTER TABLE anonymous
    MODIFY COLUMN palette_id BIGINT NOT NULL;
