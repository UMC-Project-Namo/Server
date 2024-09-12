-- V1.0.2__Add_And_Modify_columns_to_anonymous_table

-- nameVisible 컬럼을 NULL 허용으로 변경
ALTER TABLE anonymous
    MODIFY COLUMN name_visible BOOLEAN NULL;

-- name 컬럼을 NULL 허용으로 변경
ALTER TABLE anonymous
    MODIFY COLUMN name BOOLEAN NULL;

-- password 컬럼 추가
ALTER TABLE anonymous
    ADD COLUMN password VARCHAR(50) NOT NULL;

-- invite_code 추가
ALTER TABLE anonymous
    ADD COLUMN invite_code VARCHAR(50) NOT NULL;
