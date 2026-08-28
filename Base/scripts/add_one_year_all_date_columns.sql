-- =============================================================================
-- 将当前库中所有 DATE / DATETIME / TIMESTAMP 列的值整体加 1 年
-- 数据库：MySQL 5.7+ / 8.0+
--
-- 使用前请先：全库备份（mysqldump 等），并在测试库验证。
-- 执行前请先：USE your_database; 或下面取消注释并改成库名。
-- =============================================================================

-- USE wdd;

-- -----------------------------------------------------------------------------
-- 方式 A：仅预览将要执行的 UPDATE（推荐先跑，把结果复制出来检查）
-- -----------------------------------------------------------------------------
SELECT CONCAT(
    'UPDATE `', c.TABLE_NAME, '` SET `', c.COLUMN_NAME,
    '` = DATE_ADD(`', c.COLUMN_NAME, '`, INTERVAL 1 YEAR) WHERE `',
    c.COLUMN_NAME, '` IS NOT NULL;'
) AS sql_statement
FROM information_schema.COLUMNS c
INNER JOIN information_schema.TABLES t
    ON c.TABLE_SCHEMA = t.TABLE_SCHEMA
   AND c.TABLE_NAME = t.TABLE_NAME
WHERE c.TABLE_SCHEMA = DATABASE()
  AND t.TABLE_TYPE = 'BASE TABLE'
  AND c.DATA_TYPE IN ('date', 'datetime', 'timestamp')
  -- 排除生成列（MySQL 8+ 若报错可删掉下面一行）
  AND NOT (IFNULL(c.EXTRA, '') LIKE '%GENERATED%')
ORDER BY c.TABLE_NAME, c.COLUMN_NAME;


-- -----------------------------------------------------------------------------
-- 方式 B：存储过程一次性执行（确认预览无误后再用）
-- 在客户端执行：SOURCE /path/to/this/file.sql; 然后 CALL add_one_year_all_date_columns();
-- -----------------------------------------------------------------------------
DELIMITER //

DROP PROCEDURE IF EXISTS add_one_year_all_date_columns //

CREATE PROCEDURE add_one_year_all_date_columns()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE v_table VARCHAR(64);
    DECLARE v_column VARCHAR(64);
    DECLARE cur CURSOR FOR
        SELECT c.TABLE_NAME, c.COLUMN_NAME
        FROM information_schema.COLUMNS c
        INNER JOIN information_schema.TABLES t
            ON c.TABLE_SCHEMA = t.TABLE_SCHEMA
           AND c.TABLE_NAME = t.TABLE_NAME
        WHERE c.TABLE_SCHEMA = DATABASE()
          AND t.TABLE_TYPE = 'BASE TABLE'
          AND c.DATA_TYPE IN ('date', 'datetime', 'timestamp')
          AND NOT (IFNULL(c.EXTRA, '') LIKE '%GENERATED%')
        ORDER BY c.TABLE_NAME, c.COLUMN_NAME;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    START TRANSACTION;

    OPEN cur;
    read_loop: LOOP
        FETCH cur INTO v_table, v_column;
        IF done THEN
            LEAVE read_loop;
        END IF;

        SET @sql = CONCAT(
            'UPDATE `', v_table, '` SET `', v_column,
            '` = DATE_ADD(`', v_column, '`, INTERVAL 1 YEAR) WHERE `',
            v_column, '` IS NOT NULL'
        );
        PREPARE stmt FROM @sql;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    END LOOP;
    CLOSE cur;

    COMMIT;
END //

DELIMITER ;

-- 执行（取消注释）：
-- CALL add_one_year_all_date_columns();

-- 用完可删存储过程：
-- DROP PROCEDURE IF EXISTS add_one_year_all_date_columns;
