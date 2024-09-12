-- V1.0.3__Modify_anonymous_column_from_anonymous_table

-- anonymous와 participant를  다대일에서 일대일 관계로 변경
ALTER TABLE participant
    ADD CONSTRAINT UQ_Participant_Anonymous UNIQUE (anonymous_id);
