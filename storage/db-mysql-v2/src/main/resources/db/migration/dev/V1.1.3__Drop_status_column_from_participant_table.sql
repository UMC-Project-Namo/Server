-- V1.0.6__Drop_invitation_url_column_from_schedule_table

-- invitation_url 삭제
ALTER TABLE participant
DROP COLUMN status;
