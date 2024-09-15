-- V1.0.7__Add_columns_to_participant_table

-- custom_title 컬럼 추가
ALTER TABLE participant
    ADD COLUMN custom_title VARCHAR(50) NULL,
    ADD COLUMN custom_image VARCHAR(255) NULL;

-- 기존 레코드에 대해 기본값 설정
UPDATE participant p
    LEFT JOIN schedule s ON p.schedule_id = s.id
SET p.custom_title = s.title
WHERE s.schedule_type = 1 AND p.custom_title IS NULL;

UPDATE participant p
    LEFT JOIN schedule s ON p.schedule_id = s.id
SET p.custom_image = s.image_url
WHERE s.schedule_type = 1 AND p.custom_image IS NULL;

