-- V1.0.1__Add_color_column_to_group_user_table.sql

-- Add color column for table `group_user`
ALTER TABLE `group_user`
    ADD COLUMN `color` INTEGER NOT NULL;
