-- VLESS节点表
DROP TABLE IF EXISTS `app_nodes_vless`;
CREATE TABLE `app_nodes_vless` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键，自动递增',
    `uuid` varchar(36) NOT NULL COMMENT 'VLESS链接的UUID',
    `server` varchar(15) NOT NULL COMMENT '服务器地址',
    `port` int unsigned NOT NULL COMMENT '端口号，最大值65535',
    `type` varchar(10) NOT NULL COMMENT '传输类型',
    `network` varchar(10) DEFAULT NULL COMMENT '网络类型（如TCP, HTTP, gRPC等）',
    `udp` tinyint DEFAULT '0' COMMENT '是否支持UDP',
    `tls` tinyint DEFAULT '0' COMMENT '是否使用TLS',
    `server_name` varchar(50) DEFAULT NULL COMMENT 'SNI',
    `grpc_service_name` varchar(50) DEFAULT NULL COMMENT 'gRPC服务名称，如果适用',
    `security` varchar(20) DEFAULT NULL COMMENT '加密方式，如reality',
    `public_key` varchar(255) DEFAULT NULL COMMENT '公钥，用于REALITY加密',
    `short_id` varchar(50) DEFAULT NULL COMMENT '短ID，用于REALITY加密',
    `client_fingerprint` varchar(20) DEFAULT NULL COMMENT '客户端指纹策略',
    `name` varchar(100) NOT NULL COMMENT '节点名称',
    `status` tinyint(4) DEFAULT '1' COMMENT '状态（0停用 1启用）',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='VLESS节点表';

-- Token表
DROP TABLE IF EXISTS `app_tokens`;
CREATE TABLE `app_tokens` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `token` varchar(100) NOT NULL COMMENT '唯一的Token值',
    `expiration_date` datetime DEFAULT NULL COMMENT 'Token到期时间，NULL表示永不过期',
    `status` tinyint(4) DEFAULT '1' COMMENT '状态（0停用 1启用）',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_token` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Token表';

-- Token与节点关联表
DROP TABLE IF EXISTS `app_token_nodes`;
CREATE TABLE `app_token_nodes` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `token_id` bigint(20) NOT NULL COMMENT 'Token ID',
    `node_id` bigint(20) NOT NULL COMMENT '节点ID',
    `status` tinyint(4) DEFAULT '1' COMMENT '状态（0停用 1启用）',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Token与节点关联表';

-- 分流规则表
DROP TABLE IF EXISTS `app_rule_sets`;
CREATE TABLE `app_rule_sets` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `type` tinyint NOT NULL COMMENT '规则类型，1代表custom，2代表reject，3代表direct',
    `domain` varchar(255) NOT NULL COMMENT '规则的域名部分',
    `status` tinyint(4) DEFAULT '1' COMMENT '状态（0停用 1启用）',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分流规则表'; 