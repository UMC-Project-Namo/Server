-- V1.0.4__Add_is_included_in_settlement_column_to_activity_participant_table

-- is_included_in_settlement 컬럼 추가
ALTER TABLE activity_participant
    ADD COLUMN is_included_in_settlement BOOLEAN;
