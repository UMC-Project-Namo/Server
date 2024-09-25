-- v1.0.9__Add_columns_to_member_table

-- 유저 프로필 이미지 컬럼 추가
ALTER TABLE member
    ADD COLUMN profile_image VARCHAR(255);
