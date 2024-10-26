-- V1.1.4__Add_notification_status_column_to_device_table

-- 1. notification_enabled 컬럼 추가
ALTER TABLE member
    ADD COLUMN notification_enabled BOOLEAN NULL;

-- 2. 기존 회원들의 notification_enabled 상태를 false로 설정
UPDATE member
SET notification_enabled = false
WHERE notification_enabled IS NULL;

-- 3. notification_enabled 컬럼을 NOT NULL로 변경
ALTER TABLE member
    MODIFY COLUMN notification_enabled BOOLEAN NOT NULL DEFAULT false;
