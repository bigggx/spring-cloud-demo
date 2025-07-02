CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `birth_date` date DEFAULT NULL,
  `registration_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `last_login_time` datetime DEFAULT NULL,
  `status` tinyint DEFAULT '1' COMMENT '1-active, 0-inactive',
  `address` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  KEY `idx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



CREATE PROCEDURE generate_users(IN num_rows INT)
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE batch_size INT DEFAULT 1000; -- 每批插入量
    DECLARE batch_count INT DEFAULT CEIL(num_rows / batch_size);
    DECLARE current_batch INT DEFAULT 1;

    WHILE current_batch <= batch_count DO
            START TRANSACTION;

            INSERT INTO `user` (`username`, `password`, `email`, `phone`, `birth_date`, `last_login_time`, `status`, `address`)
            SELECT
                CONCAT('user', i + ((current_batch-1)*batch_size)),
                CONCAT('pwd', FLOOR(RAND() * 1000000)),
                CONCAT('user', i + ((current_batch-1)*batch_size), '@example.com'),
                CONCAT('1', LPAD(FLOOR(RAND() * 9999999999), 10, '0')),
                DATE_SUB(CURRENT_DATE(), INTERVAL FLOOR(RAND() * 365 * 50) DAY),
                DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365 * 2) DAY),
                IF(RAND() > 0.1, 1, 0),
                CONCAT(FLOOR(RAND() * 1000), ' ',
                       ELT(FLOOR(RAND() * 5) + 1, 'Main St', 'Oak Ave', 'Pine Rd', 'Maple Blvd', 'Cedar Ln'), ', ',
                       ELT(FLOOR(RAND() * 5) + 1, 'New York', 'Los Angeles', 'Chicago', 'Houston', 'Phoenix'))
            FROM
                (SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5) t1,
                (SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5) t2,
                (SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5) t3,
                (SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5) t4
            LIMIT batch_size;

            SET current_batch = current_batch + 1;
            COMMIT;

            -- 每插入10万行输出一次进度
            IF current_batch % 100 = 0 THEN
                SELECT CONCAT('已插入: ', current_batch * batch_size, ' 行') AS progress;
            END IF;
        END WHILE;
END;

-- 执行存储过程生成1000万数据
CALL generate_users(10000000);