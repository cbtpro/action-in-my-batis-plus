DROP TABLE IF EXISTS `t_base_user`;

CREATE TABLE t_base_user (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(64) NOT NULL COMMENT '用户名',
    username_reversal VARCHAR(64) NOT NULL COMMENT '反转用户名',
    age INT DEFAULT NULL COMMENT '年龄',
    email VARCHAR(128) DEFAULT NULL COMMENT '邮箱',
    email_reversal VARCHAR(64) NOT NULL COMMENT '反转邮箱',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标志：0-未删除，1-已删除',
    PRIMARY KEY (id),
    INDEX idx_username (username),
    INDEX idx_username_reversal (username_reversal),
    INDEX idx_email (email),
    INDEX idx_email_reversal (email_reversal)
) COMMENT='用户基础信息表';
