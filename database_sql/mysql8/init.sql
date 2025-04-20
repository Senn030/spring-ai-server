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