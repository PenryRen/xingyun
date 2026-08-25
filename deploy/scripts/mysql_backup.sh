#!/bin/bash
# ============================================================
# 数据库自动备份脚本（每天跑一次，保留最近 7 天）
# 放到 crontab：
#   crontab -e
#   30 2 * * * /opt/linhang/scripts/mysql_backup.sh >> /var/log/xingyun-backup.log 2>&1
# ============================================================

set -euo pipefail

# ----- 配置 -----
BACKUP_DIR="/opt/Xingyun/backup/mysql"
MYSQL_USER="wdd_app"
MYSQL_PWD="Your_Str0ng_P@ssword"          # ← 改成安全加固后的 wdd_app 密码
DATABASE="wdd"
KEEP_DAYS=7

# 日期戳
DATE="$(date +%Y%m%d_%H%M%S)"
FILE="${BACKUP_DIR}/${DATABASE}_${DATE}.sql.gz"

mkdir -p "${BACKUP_DIR}"

# 使用 --single-transaction 保证 InnoDB 一致性；不需要锁表
echo "[$(date +'%H:%M:%S')] 开始备份数据库 ${DATABASE} → ${FILE}"
mysqldump \
    -u "${MYSQL_USER}" -p"${MYSQL_PWD}" \
    --default-character-set=utf8mb4 \
    --single-transaction \
    --routines --triggers --events \
    "${DATABASE}" \
  | gzip -c > "${FILE}"

echo "[$(date +'%H:%M:%S')] 完成，文件大小：$(du -h "${FILE}" | cut -f1)"

# 清理超过 KEEP_DAYS 天的旧备份
find "${BACKUP_DIR}" -name "${DATABASE}_*.sql.gz" -mtime +${KEEP_DAYS} -delete
echo "[$(date +'%H:%M:%S')] 已清理 ${KEEP_DAYS} 天前的旧备份"

# 可选：如果服务器装了 ossutil / 七牛等，把备份上传到云存储
# ossutil cp "${FILE}" oss://你的备份桶/mysql-backup/
# ossutil cp "${FILE}" oss://你的备份桶/mysql-backup/
