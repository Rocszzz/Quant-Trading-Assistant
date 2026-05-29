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

CREATE TABLE IF NOT EXISTS `strategy` (
    `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` bigint unsigned NOT NULL COMMENT '用户ID',
    `name` varchar(64) NOT NULL COMMENT '策略名称',
    `code` varchar(64) NOT NULL COMMENT '策略编码',
    `type` varchar(32) NOT NULL COMMENT '策略类型：MA_CROSS双均线、MACD、BREAKOUT突破',
    `description` varchar(512) DEFAULT NULL COMMENT '策略说明',
    `enabled` tinyint unsigned NOT NULL DEFAULT 1 COMMENT '是否启用：1启用，0停用',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_code` (`user_id`, `code`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='策略配置表';

CREATE TABLE IF NOT EXISTS `strategy_param` (
    `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `strategy_id` bigint unsigned NOT NULL COMMENT '策略ID',
    `param_key` varchar(64) NOT NULL COMMENT '参数键',
    `param_value` varchar(256) NOT NULL COMMENT '参数值',
    `param_type` varchar(32) NOT NULL COMMENT '参数类型：NUMBER、STRING、BOOLEAN',
    `remark` varchar(128) DEFAULT NULL COMMENT '参数说明',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_strategy_param_key` (`strategy_id`, `param_key`),
    KEY `idx_strategy_id` (`strategy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='策略参数表';

CREATE TABLE IF NOT EXISTS `market_quote` (
    `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `symbol` varchar(32) NOT NULL COMMENT '股票代码',
    `trade_date` date NOT NULL COMMENT '交易日期',
    `open_price` decimal(18, 4) NOT NULL COMMENT '开盘价',
    `high_price` decimal(18, 4) NOT NULL COMMENT '最高价',
    `low_price` decimal(18, 4) NOT NULL COMMENT '最低价',
    `close_price` decimal(18, 4) NOT NULL COMMENT '收盘价',
    `pre_close_price` decimal(18, 4) NOT NULL COMMENT '前收盘价',
    `volume` bigint unsigned NOT NULL DEFAULT 0 COMMENT '成交量',
    `amount` decimal(20, 4) NOT NULL DEFAULT 0.0000 COMMENT '成交额',
    `change_rate` decimal(10, 4) NOT NULL DEFAULT 0.0000 COMMENT '涨跌幅百分比',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_symbol_trade_date` (`symbol`, `trade_date`),
    KEY `idx_symbol_trade_date` (`symbol`, `trade_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='行情数据表';

INSERT INTO `user` (`username`, `password`, `nickname`)
VALUES ('admin', SHA2('admin123', 256), '量化助手演示用户')
ON DUPLICATE KEY UPDATE
    `password` = IF(`password` = 'admin123', VALUES(`password`), `password`),
    `nickname` = VALUES(`nickname`);

INSERT INTO `strategy` (`user_id`, `name`, `code`, `type`, `description`, `enabled`)
SELECT `id`, '默认双均线策略模板', 'ma_cross_default', 'MA_CROSS',
       '短期均线上穿长期均线时形成买入观察信号，仅用于策略配置和后续回测，不执行真实交易。', 1
FROM `user`
WHERE `username` = 'admin'
ON DUPLICATE KEY UPDATE
    `name` = VALUES(`name`),
    `type` = VALUES(`type`),
    `description` = VALUES(`description`),
    `enabled` = VALUES(`enabled`);

INSERT INTO `strategy_param` (`strategy_id`, `param_key`, `param_value`, `param_type`, `remark`)
SELECT `id`, 'shortPeriod', '5', 'NUMBER', '短期均线周期'
FROM `strategy`
WHERE `code` = 'ma_cross_default'
  AND `user_id` = (SELECT `id` FROM `user` WHERE `username` = 'admin')
ON DUPLICATE KEY UPDATE
    `param_value` = VALUES(`param_value`),
    `param_type` = VALUES(`param_type`),
    `remark` = VALUES(`remark`);

INSERT INTO `strategy_param` (`strategy_id`, `param_key`, `param_value`, `param_type`, `remark`)
SELECT `id`, 'longPeriod', '20', 'NUMBER', '长期均线周期'
FROM `strategy`
WHERE `code` = 'ma_cross_default'
  AND `user_id` = (SELECT `id` FROM `user` WHERE `username` = 'admin')
ON DUPLICATE KEY UPDATE
    `param_value` = VALUES(`param_value`),
    `param_type` = VALUES(`param_type`),
    `remark` = VALUES(`remark`);

INSERT INTO `strategy_param` (`strategy_id`, `param_key`, `param_value`, `param_type`, `remark`)
SELECT `id`, 'signalMode', 'CROSS_UP', 'STRING', '信号模式：短线上穿长线'
FROM `strategy`
WHERE `code` = 'ma_cross_default'
  AND `user_id` = (SELECT `id` FROM `user` WHERE `username` = 'admin')
ON DUPLICATE KEY UPDATE
    `param_value` = VALUES(`param_value`),
    `param_type` = VALUES(`param_type`),
    `remark` = VALUES(`remark`);

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

INSERT INTO `market_quote` (
    `symbol`, `trade_date`, `open_price`, `high_price`, `low_price`, `close_price`,
    `pre_close_price`, `volume`, `amount`, `change_rate`
)
VALUES
    -- 演示行情数据，仅用于本地查看 K 线和指标效果，不代表真实证券行情。
    ('000001', '2026-05-04', 12.0500, 12.1800, 11.9600, 12.1200, 12.0000, 986000, 11950320.0000, 1.0000),
    ('000001', '2026-05-05', 12.1300, 12.2600, 12.0200, 12.2000, 12.1200, 1032000, 12590400.0000, 0.6601),
    ('000001', '2026-05-06', 12.2100, 12.3800, 12.1100, 12.3100, 12.2000, 1110000, 13664100.0000, 0.9016),
    ('000001', '2026-05-07', 12.2800, 12.3500, 12.0600, 12.1400, 12.3100, 980000, 11897200.0000, -1.3810),
    ('000001', '2026-05-08', 12.1500, 12.4200, 12.1000, 12.3600, 12.1400, 1280000, 15820800.0000, 1.8122),
    ('000001', '2026-05-11', 12.3700, 12.5500, 12.3000, 12.5000, 12.3600, 1325000, 16562500.0000, 1.1327),
    ('000001', '2026-05-12', 12.5100, 12.6400, 12.4300, 12.5800, 12.5000, 1210000, 15221800.0000, 0.6400),
    ('000001', '2026-05-13', 12.5600, 12.7000, 12.4100, 12.4500, 12.5800, 1098000, 13670100.0000, -1.0334),
    ('000001', '2026-05-14', 12.4600, 12.6200, 12.3300, 12.6000, 12.4500, 1156000, 14565600.0000, 1.2048),
    ('000001', '2026-05-15', 12.6100, 12.8000, 12.5200, 12.7400, 12.6000, 1402000, 17861480.0000, 1.1111),
    ('000001', '2026-05-18', 12.7200, 12.9000, 12.6600, 12.8200, 12.7400, 1510000, 19358200.0000, 0.6280),
    ('000001', '2026-05-19', 12.8000, 12.8800, 12.6000, 12.6800, 12.8200, 1265000, 16040200.0000, -1.0920),
    ('000001', '2026-05-20', 12.6900, 12.8600, 12.6100, 12.7900, 12.6800, 1188000, 15194520.0000, 0.8675),
    ('000001', '2026-05-21', 12.8100, 12.9800, 12.7300, 12.9100, 12.7900, 1346000, 17375860.0000, 0.9382),
    ('000001', '2026-05-22', 12.9000, 13.0500, 12.8200, 12.9800, 12.9100, 1420000, 18431600.0000, 0.5422),
    ('000001', '2026-05-25', 13.0000, 13.1600, 12.9200, 13.1000, 12.9800, 1550000, 20305000.0000, 0.9245),
    ('000001', '2026-05-26', 13.0800, 13.2200, 12.9600, 13.0200, 13.1000, 1370000, 17837400.0000, -0.6107),
    ('000001', '2026-05-27', 13.0300, 13.2400, 12.9900, 13.1800, 13.0200, 1495000, 19704100.0000, 1.2289),
    ('000001', '2026-05-28', 13.1600, 13.3200, 13.0500, 13.2600, 13.1800, 1620000, 21481200.0000, 0.6070),
    ('000001', '2026-05-29', 13.2800, 13.4800, 13.1800, 13.4200, 13.2600, 1780000, 23887600.0000, 1.2066),
    ('600519', '2026-05-04', 1680.0000, 1698.5000, 1668.0000, 1690.2000, 1675.0000, 82000, 138596400.0000, 0.9075),
    ('600519', '2026-05-05', 1691.0000, 1705.0000, 1682.3000, 1698.8000, 1690.2000, 79000, 134205200.0000, 0.5088),
    ('600519', '2026-05-06', 1699.5000, 1718.6000, 1690.0000, 1712.4000, 1698.8000, 86000, 147266400.0000, 0.8006),
    ('600519', '2026-05-07', 1710.0000, 1715.2000, 1688.0000, 1695.6000, 1712.4000, 91000, 154299600.0000, -0.9811),
    ('600519', '2026-05-08', 1696.5000, 1720.0000, 1693.0000, 1716.8000, 1695.6000, 94000, 161579200.0000, 1.2503),
    ('600519', '2026-05-11', 1718.0000, 1736.5000, 1710.0000, 1730.5000, 1716.8000, 102000, 176511000.0000, 0.7980),
    ('600519', '2026-05-12', 1732.0000, 1740.0000, 1721.3000, 1728.6000, 1730.5000, 88000, 152116800.0000, -0.1098),
    ('600519', '2026-05-13', 1729.0000, 1745.2000, 1718.0000, 1738.2000, 1728.6000, 93000, 161652600.0000, 0.5554),
    ('600519', '2026-05-14', 1737.0000, 1752.0000, 1725.0000, 1749.6000, 1738.2000, 97000, 169711200.0000, 0.6559),
    ('600519', '2026-05-15', 1750.0000, 1765.5000, 1738.8000, 1758.4000, 1749.6000, 105000, 184632000.0000, 0.5030),
    ('600519', '2026-05-18', 1759.0000, 1778.0000, 1751.2000, 1769.8000, 1758.4000, 112000, 198217600.0000, 0.6483),
    ('600519', '2026-05-19', 1768.0000, 1774.6000, 1749.5000, 1755.2000, 1769.8000, 98000, 171009600.0000, -0.8250),
    ('600519', '2026-05-20', 1756.0000, 1782.3000, 1750.0000, 1776.6000, 1755.2000, 116000, 206085600.0000, 1.2192),
    ('600519', '2026-05-21', 1778.0000, 1795.0000, 1768.5000, 1788.2000, 1776.6000, 121000, 216472200.0000, 0.6529),
    ('600519', '2026-05-22', 1786.0000, 1802.5000, 1772.0000, 1779.5000, 1788.2000, 108000, 192186000.0000, -0.4865),
    ('600519', '2026-05-25', 1780.0000, 1810.0000, 1778.0000, 1805.6000, 1779.5000, 132000, 238339200.0000, 1.4667),
    ('600519', '2026-05-26', 1806.0000, 1820.5000, 1795.0000, 1812.4000, 1805.6000, 126000, 228362400.0000, 0.3766),
    ('600519', '2026-05-27', 1810.0000, 1818.0000, 1788.6000, 1799.8000, 1812.4000, 119000, 214176200.0000, -0.6952),
    ('600519', '2026-05-28', 1800.0000, 1826.8000, 1794.0000, 1822.6000, 1799.8000, 135000, 246051000.0000, 1.2668),
    ('600519', '2026-05-29', 1824.0000, 1840.0000, 1812.5000, 1835.8000, 1822.6000, 142000, 260683600.0000, 0.7242),
    ('000002', '2026-05-29', 8.3500, 8.5200, 8.2600, 8.4700, 8.3100, 2680000, 22699600.0000, 1.9254),
    ('000063', '2026-05-29', 29.8000, 30.5000, 29.5500, 30.2200, 29.7600, 3120000, 94300000.0000, 1.5457),
    ('000333', '2026-05-29', 68.2000, 69.1500, 67.9000, 68.8800, 68.1000, 890000, 61232000.0000, 1.1454),
    ('000858', '2026-05-29', 142.6000, 144.8000, 141.9000, 143.7500, 142.2000, 760000, 109250000.0000, 1.0900),
    ('002415', '2026-05-29', 31.2000, 31.8800, 30.9500, 31.6000, 31.0500, 1860000, 58776000.0000, 1.7713),
    ('300750', '2026-05-29', 212.5000, 216.8000, 211.3000, 215.2000, 211.9000, 980000, 210896000.0000, 1.5573),
    ('600000', '2026-05-29', 9.1800, 9.3000, 9.1200, 9.2400, 9.1600, 2240000, 20697600.0000, 0.8734),
    ('600036', '2026-05-29', 37.6000, 38.1200, 37.3500, 37.9800, 37.5200, 1760000, 66844800.0000, 1.2260),
    ('600900', '2026-05-29', 28.4000, 28.7600, 28.2200, 28.6500, 28.3800, 1580000, 45267000.0000, 0.9514),
    ('601318', '2026-05-29', 49.8000, 50.5600, 49.5000, 50.2200, 49.7000, 2010000, 100942200.0000, 1.0463)
ON DUPLICATE KEY UPDATE
    `open_price` = VALUES(`open_price`),
    `high_price` = VALUES(`high_price`),
    `low_price` = VALUES(`low_price`),
    `close_price` = VALUES(`close_price`),
    `pre_close_price` = VALUES(`pre_close_price`),
    `volume` = VALUES(`volume`),
    `amount` = VALUES(`amount`),
    `change_rate` = VALUES(`change_rate`);
