-- V1.1.1__Modify_birthday_column_from_member_table

-- birthday DATE 타입 임시 컬럼 추가
ALTER TABLE member
    ADD COLUMN birthday_date DATE NULL DEFAULT '2000-01-01';

-- 기존 birthday 컬럼 삭제 및 새 컬럼 이름 변경
ALTER TABLE member
    DROP COLUMN birthday,
    CHANGE COLUMN birthday_date birthday DATE NULL;