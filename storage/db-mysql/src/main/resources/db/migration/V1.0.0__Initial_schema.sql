-- V1__initial_schema.sql

-- Table structure for table `user`
CREATE TABLE IF NOT EXISTS `user`
(
    `user_id`              bigint                         NOT NULL AUTO_INCREMENT,
    `created_date`         datetime(6)  DEFAULT NULL,
    `last_modified_date`   datetime(6)  DEFAULT NULL,
    `birthday`             varchar(255) DEFAULT NULL,
    `email`                varchar(50)                    NOT NULL,
    `name`                 varchar(20)                    NOT NULL,
    `refresh_token`        varchar(255) DEFAULT NULL,
    `social_refresh_token` varchar(255)                   NOT NULL,
    `social_type`          enum ('KAKAO','NAVER','APPLE') NOT NULL,
    `status`               varchar(15)  DEFAULT 'ACTIVE',
    PRIMARY KEY (`user_id`),
    UNIQUE KEY `emailAndSocialType` (`email`, `social_type`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Table structure for table `term`
CREATE TABLE IF NOT EXISTS `term`
(
    `term_id`            bigint     NOT NULL AUTO_INCREMENT,
    `created_date`       datetime(6)                                             DEFAULT NULL,
    `last_modified_date` datetime(6)                                             DEFAULT NULL,
    `content`            enum ('TERMS_OF_USE','PERSONAL_INFORMATION_COLLECTION') DEFAULT NULL,
    `is_check`           tinyint(1) NOT NULL,
    `user_id`            bigint                                                  DEFAULT NULL,
    PRIMARY KEY (`term_id`),
    KEY `FKktl98gic60ehb8miresv21f12` (`user_id`),
    CONSTRAINT `FKktl98gic60ehb8miresv21f12` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Table structure for table `moim`
CREATE TABLE IF NOT EXISTS `moim`
(
    `moim_id`            bigint NOT NULL AUTO_INCREMENT,
    `created_date`       datetime(6)                DEFAULT NULL,
    `last_modified_date` datetime(6)                DEFAULT NULL,
    `code`               varchar(255)               DEFAULT NULL,
    `img_url`            varchar(255)               DEFAULT NULL,
    `member_count`       int                        DEFAULT NULL,
    `name`               varchar(255)               DEFAULT NULL,
    `status`             enum ('INACTIVE','ACTIVE') DEFAULT NULL,
    PRIMARY KEY (`moim_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Table structure for table `moim_and_user`
CREATE TABLE IF NOT EXISTS `moim_and_user`
(
    `moim_and_user_id`   bigint NOT NULL AUTO_INCREMENT,
    `created_date`       datetime(6)  DEFAULT NULL,
    `last_modified_date` datetime(6)  DEFAULT NULL,
    `color`              int          DEFAULT NULL,
    `moim_custom_name`   varchar(255) DEFAULT NULL,
    `moim_id`            bigint       DEFAULT NULL,
    `user_id`            bigint       DEFAULT NULL,
    PRIMARY KEY (`moim_and_user_id`),
    KEY `FKrjna2f79rfktqi8fp2s4fujvw` (`moim_id`),
    KEY `FK2l93wohn2b5779njwnwp5euuy` (`user_id`),
    CONSTRAINT `FK2l93wohn2b5779njwnwp5euuy` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
    CONSTRAINT `FKrjna2f79rfktqi8fp2s4fujvw` FOREIGN KEY (`moim_id`) REFERENCES `moim` (`moim_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Table structure for table `category`
CREATE TABLE IF NOT EXISTS `category`
(
    `category_id`        bigint     NOT NULL AUTO_INCREMENT,
    `created_date`       datetime(6)                       DEFAULT NULL,
    `last_modified_date` datetime(6)                       DEFAULT NULL,
    `kind`               enum ('SCHEDULE','MOIM','CUSTOM') DEFAULT NULL,
    `name`               varchar(20)                       DEFAULT NULL,
    `share`              tinyint(1) NOT NULL,
    `status`             enum ('ACTIVE','DELETE')          DEFAULT NULL,
    `palette_id`         bigint                            DEFAULT NULL,
    `user_id`            bigint                            DEFAULT NULL,
    PRIMARY KEY (`category_id`),
    KEY `FKjfi379el7jfjr9ne4011fr7fb` (`palette_id`),
    KEY `FKpfk8djhv5natgshmxiav6xkpu` (`user_id`),
    CONSTRAINT `FKjfi379el7jfjr9ne4011fr7fb` FOREIGN KEY (`palette_id`) REFERENCES `palette` (`palette_id`),
    CONSTRAINT `FKpfk8djhv5natgshmxiav6xkpu` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Table structure for table `image`
CREATE TABLE IF NOT EXISTS `image`
(
    `image_id`           bigint NOT NULL AUTO_INCREMENT,
    `created_date`       datetime(6)  DEFAULT NULL,
    `last_modified_date` datetime(6)  DEFAULT NULL,
    `img_url`            varchar(255) DEFAULT NULL,
    `schedule_id`        bigint       DEFAULT NULL,
    PRIMARY KEY (`image_id`),
    KEY `FKi8og1kq5xa9yopghux7rrxf0g` (`schedule_id`),
    CONSTRAINT `FKi8og1kq5xa9yopghux7rrxf0g` FOREIGN KEY (`schedule_id`) REFERENCES `schedule` (`schedule_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Table structure for table `alarm`
CREATE TABLE IF NOT EXISTS `alarm`
(
    `alarm_id`           bigint NOT NULL AUTO_INCREMENT,
    `created_date`       datetime(6) DEFAULT NULL,
    `last_modified_date` datetime(6) DEFAULT NULL,
    `alarm_date`         int         DEFAULT NULL,
    `schedule_id`        bigint      DEFAULT NULL,
    PRIMARY KEY (`alarm_id`),
    KEY `FK244k7g4nbibdef4p46o3hay7d` (`schedule_id`),
    CONSTRAINT `FK244k7g4nbibdef4p46o3hay7d` FOREIGN KEY (`schedule_id`) REFERENCES `schedule` (`schedule_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Table structure for table `moim_schedule`
CREATE TABLE IF NOT EXISTS `moim_schedule`
(
    `moim_schedule_id`   bigint      NOT NULL AUTO_INCREMENT,
    `created_date`       datetime(6)               DEFAULT NULL,
    `last_modified_date` datetime(6)               DEFAULT NULL,
    `kakao_location_id`  varchar(255)              DEFAULT NULL,
    `location_name`      varchar(255)              DEFAULT NULL,
    `x`                  double                    DEFAULT NULL,
    `y`                  double                    DEFAULT NULL,
    `name`               varchar(255)              DEFAULT NULL,
    `day_interval`       int                       DEFAULT NULL,
    `end_date`           datetime(6) NOT NULL,
    `start_date`         datetime(6) NOT NULL,
    `status`             enum ('ACTIVE','DELETED') DEFAULT NULL,
    `moim_id`            bigint                    DEFAULT NULL,
    PRIMARY KEY (`moim_schedule_id`),
    KEY `FK62kfx7r8s9q066tf7ln6v4vhj` (`moim_id`),
    CONSTRAINT `FK62kfx7r8s9q066tf7ln6v4vhj` FOREIGN KEY (`moim_id`) REFERENCES `moim` (`moim_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Table structure for table `moim_schedule_alarm`
CREATE TABLE IF NOT EXISTS `moim_schedule_alarm`
(
    `moim_schedule_alarm_id` bigint NOT NULL AUTO_INCREMENT,
    `alarm_date`             int    DEFAULT NULL,
    `moim_schedule_user_id`  bigint DEFAULT NULL,
    PRIMARY KEY (`moim_schedule_alarm_id`),
    KEY `FK9s7c1m70rhigtpw6oskf0d5g8` (`moim_schedule_user_id`),
    CONSTRAINT `FK9s7c1m70rhigtpw6oskf0d5g8` FOREIGN KEY (`moim_schedule_user_id`) REFERENCES `moim_schedule_and_user` (`group_schedule_user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Table structure for table `moim_schedule_and_user`
CREATE TABLE IF NOT EXISTS `moim_schedule_and_user`
(
    `group_schedule_user_id` bigint NOT NULL AUTO_INCREMENT,
    `created_date`           datetime(6)                                               DEFAULT NULL,
    `last_modified_date`     datetime(6)                                               DEFAULT NULL,
    `memo`                   varchar(255)                                              DEFAULT NULL,
    `visible_status`         enum ('ALL','NOT_SEEN_MEMO','NOT_SEEN_PERSONAL_SCHEDULE') DEFAULT NULL,
    `category_id`            bigint                                                    DEFAULT NULL,
    `moim_schedule_id`       bigint                                                    DEFAULT NULL,
    `user_id`                bigint                                                    DEFAULT NULL,
    PRIMARY KEY (`group_schedule_user_id`),
    KEY `FKe6xp7eug28ohfmccqi3gn8hd0` (`category_id`),
    KEY `FKgtrgjgb43xr83fxoni9dgcw92` (`moim_schedule_id`),
    KEY `FKhg0xs2dldnl91240830celr2` (`user_id`),
    CONSTRAINT `FKe6xp7eug28ohfmccqi3gn8hd0` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`),
    CONSTRAINT `FKgtrgjgb43xr83fxoni9dgcw92` FOREIGN KEY (`moim_schedule_id`) REFERENCES `moim_schedule` (`moim_schedule_id`),
    CONSTRAINT `FKhg0xs2dldnl91240830celr2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Table structure for table `moim_memo`
CREATE TABLE IF NOT EXISTS `moim_memo`
(
    `moim_memo_id`       bigint NOT NULL AUTO_INCREMENT,
    `created_date`       datetime(6) DEFAULT NULL,
    `last_modified_date` datetime(6) DEFAULT NULL,
    `moim_schedule_id`   bigint      DEFAULT NULL,
    PRIMARY KEY (`moim_memo_id`),
    UNIQUE KEY `UK_o1ppin7xcgbf3qhq8l78mh3yp` (`moim_schedule_id`),
    CONSTRAINT `FK75aqqgvqsawsevfqx7pk4j786` FOREIGN KEY (`moim_schedule_id`) REFERENCES `moim_schedule` (`moim_schedule_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Table structure for table `palette`
CREATE TABLE IF NOT EXISTS `palette`
(
    `palette_id` bigint      NOT NULL,
    `belong`     varchar(10) NOT NULL,
    `color`      int         NOT NULL,
    PRIMARY KEY (`palette_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Table structure for table `schedule`
CREATE TABLE IF NOT EXISTS `schedule`
(
    `schedule_id`        bigint      NOT NULL AUTO_INCREMENT,
    `created_date`       datetime(6)               DEFAULT NULL,
    `last_modified_date` datetime(6)               DEFAULT NULL,
    `content`            varchar(255)              DEFAULT NULL,
    `day_interval`       int                       DEFAULT NULL,
    `end_date`           datetime(6) NOT NULL,
    `kakao_location_id`  varchar(255)              DEFAULT NULL,
    `location_name`      varchar(255)              DEFAULT NULL,
    `x`                  double                    DEFAULT NULL,
    `y`                  double                    DEFAULT NULL,
    `name`               varchar(255)              DEFAULT NULL,
    `start_date`         datetime(6) NOT NULL,
    `status`             enum ('ACTIVE','DELETED') DEFAULT NULL,
    `category_id`        bigint                    DEFAULT NULL,
    `user_id`            bigint                    DEFAULT NULL,
    PRIMARY KEY (`schedule_id`),
    KEY `FKcxpxcq9isndm1ccspu6ftc3iy` (`category_id`),
    KEY `FK758td0c8ulcqhjx4x4l9wdj9e` (`user_id`),
    CONSTRAINT `FK758td0c8ulcqhjx4x4l9wdj9e` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
    CONSTRAINT `FKcxpxcq9isndm1ccspu6ftc3iy` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Table structure for table `moim_memo_location`
CREATE TABLE IF NOT EXISTS `moim_memo_location`
(
    `moim_memo_location_id` bigint NOT NULL AUTO_INCREMENT,
    `created_date`          datetime(6)  DEFAULT NULL,
    `last_modified_date`    datetime(6)  DEFAULT NULL,
    `kakao_location_id`     varchar(255) DEFAULT NULL,
    `location_name`         varchar(255) DEFAULT NULL,
    `x`                     double       DEFAULT NULL,
    `y`                     double       DEFAULT NULL,
    `moim_id`               bigint       DEFAULT NULL,
    PRIMARY KEY (`moim_memo_location_id`),
    KEY `FKqxqxqxqxqxqxqxqxqxqxqxqxq` (`moim_id`),
    CONSTRAINT `FKqxqxqxqxqxqxqxqxqxqxqxqxq` FOREIGN KEY (`moim_id`) REFERENCES `moim` (`moim_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Table structure for table `moim_memo_location_and_user`
CREATE TABLE IF NOT EXISTS `moim_memo_location_and_user`
(
    `moim_memo_location_and_user_id` bigint NOT NULL AUTO_INCREMENT,
    `created_date`                   datetime(6)  DEFAULT NULL,
    `last_modified_date`             datetime(6)  DEFAULT NULL,
    `memo`                           varchar(255) DEFAULT NULL,
    `moim_memo_location_id`          bigint       DEFAULT NULL,
    `user_id`                        bigint       DEFAULT NULL,
    PRIMARY KEY (`moim_memo_location_and_user_id`),
    KEY `FKfxd4ff8np0iwton3jyednn3kt` (`moim_memo_location_id`),
    KEY `FKm6d9qu3ag3fd1gfb680wpbquh` (`user_id`),
    CONSTRAINT `FKfxd4ff8np0iwton3jyednn3kt` FOREIGN KEY (`moim_memo_location_id`) REFERENCES `moim_memo_location` (`moim_memo_location_id`),
    CONSTRAINT `FKm6d9qu3ag3fd1gfb680wpbquh` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Table structure for table `moim_memo_location_img`
CREATE TABLE IF NOT EXISTS `moim_memo_location_img`
(
    `moim_memo_location_img_id` bigint NOT NULL AUTO_INCREMENT,
    `created_date`              datetime(6)  DEFAULT NULL,
    `last_modified_date`        datetime(6)  DEFAULT NULL,
    `url`                       varchar(255) DEFAULT NULL,
    `moim_memo_location_id`     bigint       DEFAULT NULL,
    PRIMARY KEY (`moim_memo_location_img_id`),
    KEY `FKbc708qsfy4l64vu9u4546uik9` (`moim_memo_location_id`),
    CONSTRAINT `FKbc708qsfy4l64vu9u4546uik9` FOREIGN KEY (`moim_memo_location_id`) REFERENCES `moim_memo_location` (`moim_memo_location_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
