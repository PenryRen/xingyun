#!/usr/bin/env bash
# 在 Linux 上安装 MySQL、Redis，创建业务账号并导入 Base/init.sql
# 需要 root 或 sudo。用法: sudo ./install-linux-deps.sh
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ENV_FILE="${SCRIPT_DIR}/deps.env"
if [[ -f "${ENV_FILE}" ]]; then
  # shellcheck source=/dev/null
  source "${ENV_FILE}"
fi

MYSQL_DATABASE="${MYSQL_DATABASE:-wdd}"
MYSQL_USER="${MYSQL_USER:-wdd}"
MYSQL_PASSWORD="${MYSQL_PASSWORD:-wdd123}"
REDIS_PASSWORD="${REDIS_PASSWORD:-redis123}"
REDIS_PORT="${REDIS_PORT:-6379}"
INIT_SQL="${INIT_SQL:-${SCRIPT_DIR}/init.sql}"
SKIP_IMPORT_SQL="${SKIP_IMPORT_SQL:-0}"

log() { echo "[install-linux-deps] $*"; }
die() { echo "[install-linux-deps] 错误: $*" >&2; exit 1; }

if [[ "$(id -u)" -ne 0 ]]; then
  die "请使用 root 运行，例如: sudo $0"
fi

detect_pkg_mgr() {
  if command -v apt-get >/dev/null 2>&1; then echo apt
  elif command -v dnf >/dev/null 2>&1; then echo dnf
  elif command -v yum >/dev/null 2>&1; then echo yum
  elif command -v zypper >/dev/null 2>&1; then echo zypper
  else die "未检测到支持的包管理器 (apt/dnf/yum/zypper)"
  fi
}

PKG_MGR="$(detect_pkg_mgr)"

install_mysql_server() {
  case "${PKG_MGR}" in
    apt)
      export DEBIAN_FRONTEND=noninteractive
      apt-get update -y
      apt-get install -y mysql-server
      ;;
    dnf|yum)
      "${PKG_MGR}" install -y mysql-server
      ;;
    zypper)
      zypper --non-interactive install -y mysql
      ;;
  esac
}

install_redis() {
  case "${PKG_MGR}" in
    apt)
      apt-get install -y redis-server
      ;;
    dnf|yum)
      "${PKG_MGR}" install -y redis
      ;;
    zypper)
      zypper --non-interactive install -y redis
      ;;
  esac
}

mysql_service_name() {
  if systemctl list-unit-files 2>/dev/null | grep -q '^mysql\.service'; then
    echo mysql
  elif systemctl list-unit-files 2>/dev/null | grep -q '^mysqld\.service'; then
    echo mysqld
  elif systemctl list-unit-files 2>/dev/null | grep -q '^mariadb\.service'; then
    echo mariadb
  else
    echo mysql
  fi
}

redis_service_name() {
  if systemctl list-unit-files 2>/dev/null | grep -q '^redis-server\.service'; then
    echo redis-server
  elif systemctl list-unit-files 2>/dev/null | grep -q '^redis\.service'; then
    echo redis
  else
    echo redis
  fi
}

redis_conf_path() {
  for p in /etc/redis/redis.conf /etc/redis.conf; do
    if [[ -f "$p" ]]; then echo "$p"; return; fi
  done
  die "未找到 redis.conf（尝试过 /etc/redis/redis.conf、/etc/redis.conf）"
}

mysql_exec() {
  # 优先 socket 认证（常见 Ubuntu：root 本地免密）
  if mysql -u root -e "SELECT 1" >/dev/null 2>&1; then
    mysql -u root -e "$1"
    return
  fi
  if mysql -e "SELECT 1" >/dev/null 2>&1; then
    mysql -e "$1"
    return
  fi
  if [[ -n "${MYSQL_ROOT_PASSWORD:-}" ]]; then
    mysql -u root -p"${MYSQL_ROOT_PASSWORD}" -e "$1"
    return
  fi
  die "无法以 root 连接 MySQL。可设置环境变量 MYSQL_ROOT_PASSWORD 后重试。"
}

setup_mysql_user_and_db() {
  local esc
  esc="${MYSQL_PASSWORD//\'/\'\\\'\'}"
  mysql_exec "CREATE DATABASE IF NOT EXISTS \`${MYSQL_DATABASE}\` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
  mysql_exec "CREATE USER IF NOT EXISTS '${MYSQL_USER}'@'localhost' IDENTIFIED BY '${esc}';"
  mysql_exec "CREATE USER IF NOT EXISTS '${MYSQL_USER}'@'127.0.0.1' IDENTIFIED BY '${esc}';"
  mysql_exec "GRANT ALL PRIVILEGES ON \`${MYSQL_DATABASE}\`.* TO '${MYSQL_USER}'@'localhost';"
  mysql_exec "GRANT ALL PRIVILEGES ON \`${MYSQL_DATABASE}\`.* TO '${MYSQL_USER}'@'127.0.0.1';"
  mysql_exec "FLUSH PRIVILEGES;"
}

import_init_sql() {
  [[ -f "${INIT_SQL}" ]] || die "找不到初始化 SQL 文件: ${INIT_SQL}"
  log "导入 ${INIT_SQL}（可能需数分钟）..."
  MYSQL_PWD="${MYSQL_PASSWORD}" mysql -h 127.0.0.1 -P 3306 -u "${MYSQL_USER}" "${MYSQL_DATABASE}" < "${INIT_SQL}"
}

setup_redis_password() {
  local conf rp
  conf="$(redis_conf_path)"
  rp="${REDIS_PASSWORD//\'/\'\\\'\'}"
  if grep -qE '^[[:space:]]*requirepass[[:space:]]+' "${conf}"; then
    sed -i -E "s/^[[:space:]]*requirepass[[:space:]].*/requirepass ${rp}/" "${conf}"
  elif grep -qE '^# requirepass' "${conf}"; then
    sed -i -E "s/^# requirepass.*/requirepass ${rp}/" "${conf}"
  else
    printf '\nrequirepass %s\n' "${rp}" >> "${conf}"
  fi
  # 与 application-prod 中端口一致（若发行版默认非 6379）
  if grep -qE '^[[:space:]]*port[[:space:]]+' "${conf}"; then
    sed -i -E "s/^[[:space:]]*port[[:space:]].*/port ${REDIS_PORT}/" "${conf}"
  elif grep -qE '^# port' "${conf}"; then
    sed -i -E "s/^# port.*/port ${REDIS_PORT}/" "${conf}"
  else
    printf '\nport %s\n' "${REDIS_PORT}" >> "${conf}"
  fi
}

enable_start() {
  local svc="$1"
  systemctl enable "${svc}" 2>/dev/null || true
  systemctl restart "${svc}"
  systemctl is-active --quiet "${svc}" || die "服务 ${svc} 未运行"
}

main() {
  log "包管理器: ${PKG_MGR}"
  log "安装 MySQL Server..."
  install_mysql_server
  MSVC="$(mysql_service_name)"
  enable_start "${MSVC}"
  sleep 2

  log "配置数据库与用户 ${MYSQL_USER} / 库 ${MYSQL_DATABASE}..."
  setup_mysql_user_and_db
  if [[ "${SKIP_IMPORT_SQL}" == "1" ]]; then
    log "已设置 SKIP_IMPORT_SQL=1，跳过 init.sql 导入。"
  else
    import_init_sql
  fi

  log "安装 Redis..."
  install_redis
  RVC="$(redis_service_name)"
  log "写入 Redis requirepass..."
  setup_redis_password
  enable_start "${RVC}"

  log "完成。"
  log "MySQL: jdbc:mysql://127.0.0.1:3306/${MYSQL_DATABASE} 用户 ${MYSQL_USER}"
  log "Redis: 127.0.0.1:${REDIS_PORT}，需密码认证（与 application-prod.yml 中 spring.redis 一致）"
}

main "$@"
