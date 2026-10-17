-- ============================================================
-- MySQL 数据库安全加固脚本（防止数据库被清空）
-- 执行方式（以 root 身份）：
--   mysql --default-character-set=utf8mb4 -u root -p < security_hardening.sql
-- 注意：执行前先修改下方的两处「Your_Str0ng_P@ssword」为真实强密码
-- ============================================================

-- ------------------------------------------------------------
-- 0. 开启通用日志审计（可选，推荐开启。开启后所有 SQL 都会被记录）
--    会占用磁盘空间，建议只保留最近 7 天日志，配合 logrotate 使用
-- ------------------------------------------------------------
-- SET GLOBAL general_log = 'ON';
-- SET GLOBAL general_log_file = '/var/log/mysql/general.log';
-- SET GLOBAL log_output = 'FILE';

-- 开启慢查询日志（定位可疑操作）
SET GLOBAL slow_query_log = 'ON';
SET GLOBAL long_query_time = 5;   -- 超过 5 秒的查询都记录

-- 开启 binary log（用于时间点恢复，被删除时可以回滚）
-- 注意：binlog 必须在 my.cnf 中配置才能持久生效（重启不丢）
-- 建议在 /etc/mysql/my.cnf 的 [mysqld] 中加：
--   server-id = 1
--   log_bin   = /var/log/mysql/mysql-bin.log
--   binlog_format = ROW
--   expire_logs_days = 7

-- ------------------------------------------------------------
-- 1. 废弃匿名用户 / 远程 root / test 数据库
-- ------------------------------------------------------------
DELETE FROM mysql.user WHERE User='';
DELETE FROM mysql.user WHERE User='root' AND Host NOT IN ('localhost', '127.0.0.1', '::1');
DROP DATABASE IF EXISTS test;
DELETE FROM mysql.db WHERE Db='test' OR Db='test\\_%';

-- ------------------------------------------------------------
-- 2. 创建「应用专用账号」
--    ★ 千万不要在程序代码里使用 root ！！
--    之前被清空大概率是 root 密码泄露 + 外网可以直连 3306
-- ------------------------------------------------------------
-- 如果账号不存在才创建（兼容）
CREATE USER IF NOT EXISTS 'wdd_app'@'127.0.0.1' IDENTIFIED BY 'Your_Str0ng_P@ssword';

-- 只授予本项目需要的「增删改查」权限，绝对不能授予 SUPER / FILE / DROP 等
-- 禁止 DROP 权限 —— 这是防止整库被清空的关键！
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, INDEX, ALTER
   ON wdd.*
   TO 'wdd_app'@'127.0.0.1';

-- 取消不必要的全局权限（防止历史遗留）
REVOKE ALL PRIVILEGES ON *.* FROM 'wdd_app'@'127.0.0.1';
FLUSH PRIVILEGES;

-- ------------------------------------------------------------
-- 3. 把 root 密码改成强密码（如果目前是 root/root/123456/空 这种弱密码，请务必执行）
-- ------------------------------------------------------------
-- ALTER USER 'root'@'localhost' IDENTIFIED BY 'Your_R00t_Str0ng_P@ssword';

-- ------------------------------------------------------------
-- 4. 为关键表创建「防误删触发器」
--    阻止 DELETE 不带 WHERE、或恶意执行 DELETE FROM t_user; 这种操作
--    在业务代码删除单条数据时可以通过（WHERE id = ?），但整表删除会被拒
-- ------------------------------------------------------------
USE wdd;

DELIMITER //

DROP TRIGGER IF EXISTS trg_protect_t_user_delete //
CREATE TRIGGER trg_protect_t_user_delete
BEFORE DELETE ON t_user
FOR EACH ROW
BEGIN
    -- 这里无法直接判断「是否带 WHERE」，但可以限制「每批次删除量」
    -- 如果一次性删除超过 10 条用户，直接报错
    SET @_protect_del_cnt = IFNULL(@_protect_del_cnt, 0) + 1;
    IF @_protect_del_cnt > 10 THEN
        SIGNAL SQLSTATE '45000'
           SET MESSAGE_TEXT = '[安全保护] t_user 单次删除超过 10 条被阻止，如需批量删除请先禁用触发器';
    END IF;
END //

DROP TRIGGER IF EXISTS trg_protect_t_question_delete //
CREATE TRIGGER trg_protect_t_question_delete
BEFORE DELETE ON t_question
FOR EACH ROW
BEGIN
    SET @_protect_del_cnt = IFNULL(@_protect_del_cnt, 0) + 1;
    IF @_protect_del_cnt > 100 THEN
        SIGNAL SQLSTATE '45000'
           SET MESSAGE_TEXT = '[安全保护] t_question 单次删除超过 100 条被阻止';
    END IF;
END //

DROP TRIGGER IF EXISTS trg_protect_t_exam_info_delete //
CREATE TRIGGER trg_protect_t_exam_info_delete
BEFORE DELETE ON t_exam_info
FOR EACH ROW
BEGIN
    SET @_protect_del_cnt = IFNULL(@_protect_del_cnt, 0) + 1;
    IF @_protect_del_cnt > 50 THEN
        SIGNAL SQLSTATE '45000'
           SET MESSAGE_TEXT = '[安全保护] t_exam_info 单次删除超过 50 条被阻止';
    END IF;
END //

DROP TRIGGER IF EXISTS trg_protect_t_exam_score_delete //
CREATE TRIGGER trg_protect_t_exam_score_delete
BEFORE DELETE ON t_exam_score
FOR EACH ROW
BEGIN
    SET @_protect_del_cnt = IFNULL(@_protect_del_cnt, 0) + 1;
    IF @_protect_del_cnt > 100 THEN
        SIGNAL SQLSTATE '45000'
           SET MESSAGE_TEXT = '[安全保护] t_exam_score 单次删除超过 100 条被阻止';
    END IF;
END //

DELIMITER ;

-- ------------------------------------------------------------
-- 5. 验证结果：查看用户权限
-- ------------------------------------------------------------
-- SELECT User, Host, Super_priv, Drop_priv, Create_priv
--   FROM mysql.user
--  WHERE User IN ('root','wdd_app');
--
-- SHOW GRANTS FOR 'wdd_app'@'127.0.0.1';
