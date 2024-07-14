-- V1.0.0__Initial_schema.sql

-- Table structure for table `users`
CREATE TABLE users
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime              NULL,
    updated_at datetime              NULL,
    username   VARCHAR(50)           NOT NULL,
    email      VARCHAR(50)           NOT NULL,
    birthday   VARCHAR(10)           NULL,
    user_role  VARCHAR(50)           NOT NULL,
    status     VARCHAR(50)           NOT NULL,
    deleted_at datetime DEFAULT NULL NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);


-- Table structure for table `terms`
CREATE TABLE term
(
    id                 BIGINT AUTO_INCREMENT NOT NULL,
    created_at         datetime              NULL,
    updated_at         datetime              NULL,
    user_id            BIGINT                NULL,
    content            VARCHAR(250)          NULL,
    agree              TINYINT(1)            NOT NULL,
    agree_at           datetime              NULL,
    CONSTRAINT pk_term PRIMARY KEY (id)
);

ALTER TABLE term
    ADD CONSTRAINT FK_TERM_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);


-- Table structure for table `oauth`
CREATE TABLE oauth
(
    id                   BIGINT AUTO_INCREMENT NOT NULL,
    created_at           datetime              NULL,
    updated_at           datetime              NULL,
    user_id              BIGINT                NOT NULL,
    provider             VARCHAR(50)           NOT NULL,
    provider_id          VARCHAR(255)          NOT NULL,
    login_id             VARCHAR(255)          NOT NULL,
    social_refresh_token VARCHAR(255)          NULL,
    CONSTRAINT pk_oauth PRIMARY KEY (id)
);

ALTER TABLE oauth
    ADD CONSTRAINT FK_OAUTH_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);


-- Table structure for table `palette`
CREATE TABLE palette
(
    id         BIGINT      NOT NULL,
    belong     VARCHAR(10) NOT NULL,
    color      INT         NOT NULL,
    CONSTRAINT pk_palette PRIMARY KEY (id)
);


-- Table structure for table `category`
CREATE TABLE category
(
    id                 BIGINT AUTO_INCREMENT NOT NULL,
    created_at       datetime              NULL,
    updated_at         datetime              NULL,
    palette_id         BIGINT                NULL,
    user_id            BIGINT                NULL,
    name               VARCHAR(50)           NULL,
    type               VARCHAR(50)           NULL,
    order_num          INT                   NULL,
    status             VARCHAR(50)           NULL,
    CONSTRAINT pk_category PRIMARY KEY (id)
);

ALTER TABLE category
    ADD CONSTRAINT FK_CATEGORY_ON_PALETTE FOREIGN KEY (palette_id) REFERENCES palette (id);

ALTER TABLE category
    ADD CONSTRAINT FK_CATEGORY_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);


-- Table structure for table `group_info`
CREATE TABLE group_info
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    created_at  datetime              NULL,
    updated_at  datetime              NULL,
    name        VARCHAR(50)           NOT NULL,
    code        VARCHAR(50)           NOT NULL,
    profile_img VARCHAR(255)          NULL,
    status      VARCHAR(50)           NOT NULL,
    CONSTRAINT pk_group_info PRIMARY KEY (id)
);


-- Table structure for table `group_user`
CREATE TABLE group_user
(
    id                BIGINT AUTO_INCREMENT NOT NULL,
    created_at        datetime              NULL,
    updated_at        datetime              NULL,
    user_id           BIGINT                NOT NULL,
    group_id          BIGINT                NOT NULL,
    custom_group_name VARCHAR(50)           NULL,
    CONSTRAINT pk_group_user PRIMARY KEY (id)
);

ALTER TABLE group_user
    ADD CONSTRAINT FK_GROUP_USER_ON_GROUP FOREIGN KEY (group_id) REFERENCES group_info (id);

ALTER TABLE group_user
    ADD CONSTRAINT FK_GROUP_USER_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);


-- Table structure for table `schedule`
CREATE TABLE schedule
(
    id                 BIGINT AUTO_INCREMENT NOT NULL,
    created_at         datetime              NULL,
    updated_at         datetime              NULL,
    title              VARCHAR(50)           NOT NULL,
    start_date         datetime              NOT NULL,
    end_date           datetime              NOT NULL,
    longitude          DOUBLE                NULL,
    latitude           DOUBLE                NULL,
    location_name      VARCHAR(255)           NULL,
    kakao_location_id  VARCHAR(255)           NULL,
    CONSTRAINT pk_schedule PRIMARY KEY (id)
);

ALTER TABLE schedule
    ADD CONSTRAINT FK_SCHEDULE_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES category (id);

ALTER TABLE schedule
    ADD CONSTRAINT FK_SCHEDULE_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);


-- Table structure for table `personal_schedule`
CREATE TABLE personal_schedule
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    created_at  datetime              NULL,
    updated_at  datetime              NULL,
    user_id     BIGINT                NOT NULL,
    schedule_id BIGINT                NOT NULL,
    category_id BIGINT                NOT NULL,
    CONSTRAINT pk_personal_schedule PRIMARY KEY (id)
);

ALTER TABLE personal_schedule
    ADD CONSTRAINT FK_PERSONAL_SCHEDULE_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES category (id);

ALTER TABLE personal_schedule
    ADD CONSTRAINT FK_PERSONAL_SCHEDULE_ON_SCHEDULE FOREIGN KEY (schedule_id) REFERENCES schedule (id);

ALTER TABLE personal_schedule
    ADD CONSTRAINT FK_PERSONAL_SCHEDULE_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);


-- Table structure for table `meeting_schedule`
CREATE TABLE meeting_schedule
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    created_at    datetime              NULL,
    updated_at    datetime              NULL,
    user_id       BIGINT                NOT NULL,
    schedule_id   BIGINT                NOT NULL,
    category_id   BIGINT                NOT NULL,
    group_id      BIGINT                NOT NULL,
    custom_title  VARCHAR(50)           NULL,
    CONSTRAINT pk_meeting_schedule PRIMARY KEY (id)
);

ALTER TABLE meeting_schedule
    ADD CONSTRAINT FK_MEETING_SCHEDULE_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES category (id);

ALTER TABLE meeting_schedule
    ADD CONSTRAINT FK_MEETING_SCHEDULE_ON_GROUP FOREIGN KEY (group_id) REFERENCES group_info (id);

ALTER TABLE meeting_schedule
    ADD CONSTRAINT FK_MEETING_SCHEDULE_ON_SCHEDULE FOREIGN KEY (schedule_id) REFERENCES schedule (id);

ALTER TABLE meeting_schedule
    ADD CONSTRAINT FK_MEETING_SCHEDULE_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);


-- Table structure for table `diary`
CREATE TABLE diary
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    created_at    datetime              NULL,
    updated_at    datetime              NULL,
    m_schedule_id BIGINT                NULL,
    p_schedule_id BIGINT                NULL,
    memo          VARCHAR(250)          NOT NULL,
    CONSTRAINT pk_diary PRIMARY KEY (id)
);

ALTER TABLE diary
    ADD CONSTRAINT FK_DIARY_ON_M_SCHEDULE FOREIGN KEY (m_schedule_id) REFERENCES meeting_schedule (id);

ALTER TABLE diary
    ADD CONSTRAINT FK_DIARY_ON_P_SCHEDULE FOREIGN KEY (p_schedule_id) REFERENCES personal_schedule (id);


-- Table structure for table `diary_img`
CREATE TABLE diary_img
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime              NULL,
    updated_at datetime              NULL,
    diary_id   BIGINT                NOT NULL,
    img_url    VARCHAR(255)          NOT NULL,
    img_order  INT                   NOT NULL,
    CONSTRAINT pk_diary_img PRIMARY KEY (id)
);

ALTER TABLE diary_img
    ADD CONSTRAINT FK_DIARY_IMG_ON_DIARY FOREIGN KEY (diary_id) REFERENCES diary (id);


-- Table structure for table `activity`
CREATE TABLE activity
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    created_at    datetime              NULL,
    updated_at    datetime              NULL,
    m_schedule_id BIGINT                NOT NULL,
    title         VARCHAR(50)           NOT NULL,
    amount        INT                   NOT NULL,
    CONSTRAINT pk_activity PRIMARY KEY (id)
);

ALTER TABLE activity
    ADD CONSTRAINT FK_ACTIVITY_ON_M_SCHEDULE FOREIGN KEY (m_schedule_id) REFERENCES meeting_schedule (id);


-- Table structure for table `activity_user`
CREATE TABLE activity_user
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    created_at  datetime              NULL,
    updated_at  datetime              NULL,
    user_id     BIGINT                NOT NULL,
    activity_id BIGINT                NOT NULL,
    CONSTRAINT pk_activity_user PRIMARY KEY (id)
);

ALTER TABLE activity_user
    ADD CONSTRAINT FK_ACTIVITY_USER_ON_ACTIVITY FOREIGN KEY (activity_id) REFERENCES activity (id);

ALTER TABLE activity_user
    ADD CONSTRAINT FK_ACTIVITY_USER_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);


-- Table structure for table `activity_img`
CREATE TABLE activity_img
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    created_at  datetime              NULL,
    updated_at  datetime              NULL,
    diary_id    BIGINT                NULL,
    activity_id BIGINT                NOT NULL,
    img_url     VARCHAR(255)          NOT NULL,
    img_order   INT                   NOT NULL,
    CONSTRAINT pk_activity_img PRIMARY KEY (id)
);

ALTER TABLE activity_img
    ADD CONSTRAINT FK_ACTIVITY_IMG_ON_ACTIVITY FOREIGN KEY (activity_id) REFERENCES activity (id);

ALTER TABLE activity_img
    ADD CONSTRAINT FK_ACTIVITY_IMG_ON_DIARY FOREIGN KEY (diary_id) REFERENCES diary (id);
