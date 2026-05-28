CREATE DATABASE IF NOT EXISTS quant_trading_assistant
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE quant_trading_assistant;

CREATE TABLE IF NOT EXISTS `user` (
    `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username` varchar(64) NOT NULL COMMENT '用户名',
    `password` varchar(128) NOT NULL COMMENT '密码摘要',
    `nickname` varchar(64) NOT NULL COMMENT '用户昵称',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

CREATE TABLE IF NOT EXISTS `stock_info` (
    `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `symbol` varchar(32) NOT NULL COMMENT '股票代码',
    `name` varchar(64) NOT NULL COMMENT '股票名称',
    `exchange` varchar(32) NOT NULL COMMENT '交易所，示例：SSE、SZSE',
    `industry` varchar(64) DEFAULT NULL COMMENT '所属行业',
    `status` varchar(32) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE正常、INACTIVE停用',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_symbol` (`symbol`),
    KEY `idx_name` (`name`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='股票基础信息表';

CREATE TABLE IF NOT EXISTS `user_watchlist` (
    `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` bigint unsigned NOT NULL COMMENT '用户ID',
    `stock_id` bigint unsigned NOT NULL COMMENT '股票ID',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_stock` (`user_id`, `stock_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_stock_id` (`stock_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户自选股表';

INSERT INTO `user` (`username`, `password`, `nickname`)
VALUES ('admin', SHA2('admin123', 256), '量化助手演示用户')
ON DUPLICATE KEY UPDATE
    `password` = IF(`password` = 'admin123', VALUES(`password`), `password`),
    `nickname` = VALUES(`nickname`);

INSERT INTO `stock_info` (`symbol`, `name`, `exchange`, `industry`, `status`)
VALUES
    ('000001', '平安银行', 'SZSE', '银行', 'ACTIVE'),
    ('000002', '万科A', 'SZSE', '房地产开发', 'ACTIVE'),
    ('000063', '中兴通讯', 'SZSE', '通信设备', 'ACTIVE'),
    ('000333', '美的集团', 'SZSE', '白色家电', 'ACTIVE'),
    ('000858', '五粮液', 'SZSE', '白酒', 'ACTIVE'),
    ('002415', '海康威视', 'SZSE', '计算机设备', 'ACTIVE'),
    ('300750', '宁德时代', 'SZSE', '电池', 'ACTIVE'),
    ('600000', '浦发银行', 'SSE', '银行', 'ACTIVE'),
    ('600036', '招商银行', 'SSE', '银行', 'ACTIVE'),
    ('600519', '贵州茅台', 'SSE', '白酒', 'ACTIVE'),
    ('600900', '长江电力', 'SSE', '电力', 'ACTIVE'),
    ('601318', '中国平安', 'SSE', '保险', 'ACTIVE')
ON DUPLICATE KEY UPDATE
    `name` = VALUES(`name`),
    `exchange` = VALUES(`exchange`),
    `industry` = VALUES(`industry`),
    `status` = VALUES(`status`);
