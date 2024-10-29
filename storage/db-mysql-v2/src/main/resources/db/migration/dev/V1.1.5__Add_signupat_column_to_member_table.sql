-- V1.1.5__Add_signupat_column_to_member_table

-- 1. signUpAt 컬럼 추가
ALTER TABLE member
    ADD COLUMN sign_up_at DATETIME NULL;

-- 2. createdAt 값을 signUpAt에 복사
UPDATE member
SET sign_up_at = created_at;
