-- V1.1.6__Modify_columns_from_schedule_table

-- participant_count 업데이트
UPDATE schedule
SET
    participant_count = 1
WHERE
    schedule_type = 0
  AND participant_count IS NULL;

-- participant_nicknames 업데이트, 개인/생일 일정에 대해서만
UPDATE schedule s
SET s.participant_nicknames = (
    SELECT m.nickname
    FROM participant p
        JOIN member m ON p.member_id = m.id
    WHERE p.schedule_id = s.id
)
WHERE s.participant_nicknames IS NULL
  AND s.schedule_type IN (0, 2);

-- NOT NULL 변경
ALTER TABLE schedule
    MODIFY participant_nicknames VARCHAR(255) NOT NULL;

-- NOT NULL 변경
ALTER TABLE schedule
    MODIFY participant_count INT NOT NULL;
