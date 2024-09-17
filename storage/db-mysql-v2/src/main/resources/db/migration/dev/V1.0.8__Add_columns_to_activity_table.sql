-- V1.0.8__Add_columns_to_activity_table

-- longitude, latitude,location_name, kakao_location_id,
-- start_date, end_date 컬럼 추
ALTER TABLE activity
    ADD COLUMN longitude DOUBLE,
    ADD COLUMN latitude DOUBLE,
    ADD COLUMN location_name VARCHAR(255),
    ADD COLUMN kakao_location_id VARCHAR(255),
    ADD COLUMN start_date DATETIME,
    ADD COLUMN end_date DATETIME;