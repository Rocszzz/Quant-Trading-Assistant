CREATE DATABASE IF NOT EXISTS quant_trading_assistant
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE quant_trading_assistant;

CREATE TABLE IF NOT EXISTS `user` (
    `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username` varchar(64) NOT NULL COMMENT '用户名',
    `password` varchar(128) NOT NULL COMMENT '密码，毕业项目演示阶段使用，生产环境需改为强哈希',
    `nickname` varchar(64) NOT NULL COMMENT '用户昵称',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

INSERT INTO `user` (`username`, `password`, `nickname`)
SELECT 'admin', 'admin123', '量化助手演示用户'
WHERE NOT EXISTS (
    SELECT 1 FROM `user` WHERE `username` = 'admin'
);
