DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`           varchar(36)  NOT NULL,
    `created_time` datetime(6) NOT NULL,
    `edited_time`  datetime(6) NOT NULL,
    `nickname`     varchar(20)  DEFAULT NULL,
    `avatar`       varchar(255) DEFAULT NULL,
    `gender`       varchar(36)  DEFAULT NULL,
    `phone`        varchar(20)  NOT NULL,
    `password`     varchar(100) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


DROP TABLE IF EXISTS `ai_message`;
CREATE TABLE `ai_message`
(
    `id`            varchar(36) NOT NULL,
    `created_time`  datetime(6) NOT NULL,
    `edited_time`   datetime(6) NOT NULL,
    `creator_id`    varchar(32) NOT NULL,
    `editor_id`     varchar(32) NOT NULL,
    `type`          varchar(32) NOT NULL COMMENT '消息类型(用户/助手/系统)',
    `text_content`  text        NOT NULL COMMENT '消息内容',
    `medias`        json DEFAULT NULL COMMENT '媒体内容如图片链接、语音链接',
    `ai_session_id` varchar(32) NOT NULL COMMENT '会话id',
    PRIMARY KEY (`id`),
    KEY             `ai_message_ai_session_id_fk` (`ai_session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


DROP TABLE IF EXISTS `ai_session`;
CREATE TABLE `ai_session`
(
    `id`           varchar(36) NOT NULL,
    `created_time` datetime(6) NOT NULL,
    `edited_time`  datetime(6) NOT NULL,
    `creator_id`   varchar(32) NOT NULL,
    `editor_id`    varchar(32) NOT NULL,
    `name`         varchar(32) NOT NULL COMMENT '会话名称',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;