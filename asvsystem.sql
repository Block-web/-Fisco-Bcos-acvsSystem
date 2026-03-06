-- ① 用户表（三角色统一登录）
CREATE TABLE `user` (
                        `id`          BIGINT AUTO_INCREMENT PRIMARY KEY,
                        `username`    VARCHAR(50)  NOT NULL UNIQUE,
                        `password`    VARCHAR(100) NOT NULL,               -- MD5加密
                        `role`        ENUM('COLLEGE','STUDENT','COMPANY') NOT NULL,
                        `real_name`   VARCHAR(50),                         -- 真实姓名/企业名
                        `id_card`     CHAR(18) UNIQUE,                     -- 学生/院校法人身份证（脱敏）
                        `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ② 学历证书表（核心）
CREATE TABLE `certificate` (
                               `id`              BIGINT AUTO_INCREMENT PRIMARY KEY,
                               `cert_no`         CHAR(16) NOT NULL UNIQUE,         -- 证书编号（规则见下）
                               `student_name`    VARCHAR(50) NOT NULL,
                               `id_card`         CHAR(18) NOT NULL,                -- 学生身份证
                               `college_id`      BIGINT NOT NULL,                  -- 录入院校
                               `major`           VARCHAR(100),
                               `degree_type`     ENUM('专科','本科','硕士','博士'),
                               `graduation_date` DATE,
                               `pdf_path`        VARCHAR(255),                     -- oosurl路径
                               `file_hash`       CHAR(64) NOT NULL,                -- 证书 PDF sha256
                               `tx_hash`         CHAR(66),                         -- FISCO-BCOS 交易哈希
                               `status`          TINYINT DEFAULT 1,                -- 1 正常  0 已撤销
                               `create_time`     DATETIME DEFAULT CURRENT_TIMESTAMP,
                               INDEX idx_cert_no (cert_no),
                               INDEX idx_id_card (id_card),
                               CONSTRAINT fk_cert_college FOREIGN KEY (college_id) REFERENCES `user`(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ③ 验证日志表（企业每次扫码/查询都记一条）
CREATE TABLE `verify_log` (
                              `id`           BIGINT AUTO_INCREMENT PRIMARY KEY,
                              `cert_id`      BIGINT NOT NULL,
                              `company_id`   BIGINT NOT NULL,                    -- 查询企业
                              `verify_type`  ENUM('QUERY','BLOCKCHAIN') DEFAULT 'QUERY',
                              `result`       ENUM('PASS','FAIL') DEFAULT 'PASS',
                              `verify_time`  DATETIME DEFAULT CURRENT_TIMESTAMP,
                              INDEX idx_cert_id (cert_id),
                              CONSTRAINT fk_log_cert   FOREIGN KEY (cert_id)    REFERENCES `certificate`(id),
                              CONSTRAINT fk_log_company FOREIGN KEY (company_id) REFERENCES `user`(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;