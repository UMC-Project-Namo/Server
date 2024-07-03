-- V1.0.1__Add_user_role_column_to_user_table.sql

-- Add user_role column for table `user`
ALTER TABLE `user` ADD COLUMN `user_role` VARCHAR(25) DEFAULT 'USER' NOT NULL;
