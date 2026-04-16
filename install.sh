#!/usr/bin/env bash
# 麒麟智训：Linux 下一键编译、安装目录、systemd、可选 nginx 与数据库初始化。
# nginx（--with-nginx）：将 deploy/linux/nginx-linhang.conf.in 中 @@INSTALL_ROOT@@ 替换为 INSTALL_PREFIX，
# 前端静态根为 INSTALL_PREFIX/web/{ueit-user-web,ueit-admin}，上传为 INSTALL_PREFIX/resource/web-file。
# 默认交互式询问；自动化请加 -y / --non-interactive 并配合环境变量。
# 用法：
#   sudo ./install.sh
#   sudo INSTALL_PREFIX=/srv/linhang INIT_DB=1 ./install.sh -y
#   sudo ./install.sh --prefix /opt/linhang --skip-build --init-db
#   sudo ./install.sh -y --with-ai-agent --ai-agent-port 8000
#   sudo ./install.sh -y --no-infra --no-start-services
#   sudo SKIP_INSTALL_DEPS=1 ./install.sh -y   # 不自动用包管理器装系统依赖
#   sudo ./install.sh -y --install-deps         # 非交互但仍自动装系统依赖
#   ./install.sh --dry-run
#
# 非交互：-y / --non-interactive，或环境变量 NON_INTERACTIVE=1；其余变量同前（INSTALL_PREFIX、INIT_DB、MYSQL_* 等）。
# 交互模式（默认）：向导中会询问是否自动安装缺失的系统依赖；也可用 --install-deps / --skip-install-deps 或环境变量覆盖。
# WSL：JDK、Maven、Node；WSL2 systemd：/etc/wsl.conf [boot] systemd=true
set -euo pipefail

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DEPLOY_TPL="${REPO_ROOT}/deploy/linux"

INSTALL_PREFIX="${INSTALL_PREFIX:-/opt/linhang}"
RUN_USER="${RUN_USER:-linhang}"
SERVICE_NAME="${SERVICE_NAME:-linhang}"
SKIP_BUILD=false
WITH_NGINX=false
WITH_AI_AGENT=false
WITH_INFRA=true
DRY_RUN=false
AI_AGENT_PORT="${AI_AGENT_PORT:-8000}"
SKIP_INSTALL_DEPS="${SKIP_INSTALL_DEPS:-false}"
AUTO_START_SERVICES="${AUTO_START_SERVICES:-true}"
REDIS_PASSWORD="${REDIS_PASSWORD:-redis123}"
REDIS_PORT="${REDIS_PORT:-6379}"

# CLI 显式指定后，交互环节不再询问对应项
CLI_PREFIX=false
CLI_SKIP_BUILD=false
CLI_NGINX=false
CLI_AI_AGENT=false
CLI_INFRA=false
CLI_INIT_DB=false
CLI_AI_AGENT_PORT=false
CLI_SKIP_INSTALL_DEPS=false
CLI_AUTO_INSTALL_DEPS=false
CLI_AUTO_START=false

normalize_bool() {
  case "${1:-}" in
    1|true|yes|TRUE|YES) echo true ;;
    *) echo false ;;
  esac
}

NON_INTERACTIVE="$(normalize_bool "${NON_INTERACTIVE:-false}")"
INIT_DB="$(normalize_bool "${INIT_DB:-false}")"
AUTO_START_SERVICES="$(normalize_bool "${AUTO_START_SERVICES:-true}")"

usage() {
  sed -n '2,18p' "$0"
}

while [[ $# -gt 0 ]]; do
  case "$1" in
    --prefix)
      CLI_PREFIX=true
      INSTALL_PREFIX="${2:?}"
      shift 2
      ;;
    --skip-build)
      CLI_SKIP_BUILD=true
      SKIP_BUILD=true
      shift
      ;;
    --with-nginx)
      CLI_NGINX=true
      WITH_NGINX=true
      shift
      ;;
    --with-ai-agent)
      CLI_AI_AGENT=true
      WITH_AI_AGENT=true
      shift
      ;;
    --with-infra)
      CLI_INFRA=true
      WITH_INFRA=true
      shift
      ;;
    --no-infra)
      CLI_INFRA=true
      WITH_INFRA=false
      shift
      ;;
    --ai-agent-port)
      CLI_AI_AGENT_PORT=true
      AI_AGENT_PORT="${2:?}"
      shift 2
      ;;
    --dry-run)
      DRY_RUN=true
      shift
      ;;
    --init-db)
      CLI_INIT_DB=true
      INIT_DB=true
      shift
      ;;
    --no-init-db)
      CLI_INIT_DB=true
      INIT_DB=false
      shift
      ;;
    -y|--yes|--non-interactive)
      NON_INTERACTIVE=true
      shift
      ;;
    --skip-install-deps)
      CLI_SKIP_INSTALL_DEPS=true
      SKIP_INSTALL_DEPS=true
      shift
      ;;
    --install-deps|--auto-install-deps)
      CLI_AUTO_INSTALL_DEPS=true
      SKIP_INSTALL_DEPS=false
      shift
      ;;
    --start-services)
      CLI_AUTO_START=true
      AUTO_START_SERVICES=true
      shift
      ;;
    --no-start-services)
      CLI_AUTO_START=true
      AUTO_START_SERVICES=false
      shift
      ;;
    -h|--help)
      usage
      exit 0
      ;;
    *)
      echo "未知参数: $1" >&2
      usage
      exit 1
      ;;
  esac
done

# 非 TTY（如管道）且无显式 -y 时，按非交互处理并提示
if [[ "$NON_INTERACTIVE" != true ]] && [[ ! -t 0 ]]; then
  echo "提示: 标准输入非终端，按非交互模式使用环境变量与默认值（可加 -y 抑制本提示）。" >&2
  NON_INTERACTIVE=true
fi

SKIP_INSTALL_DEPS="$(normalize_bool "${SKIP_INSTALL_DEPS:-false}")"
WITH_INFRA="$(normalize_bool "${WITH_INFRA:-true}")"
AUTO_START_SERVICES="$(normalize_bool "${AUTO_START_SERVICES:-true}")"

run() {
  if [[ "$DRY_RUN" == true ]]; then
    printf '[dry-run] '; printf '%q ' "$@"; echo
  else
    "$@"
  fi
}

read_line() {
  local prompt="$1" var="$2"
  local def reply
  eval "def=\${${var}-}"
  read -r -p "${prompt} [${def}]: " reply || true
  if [[ -n "${reply}" ]]; then
    printf -v "$var" '%s' "$reply"
  fi
}

read_yesno() {
  local var="$1" default="$2" msg="$3"
  local hint="Y/n"
  [[ "$default" == "n" ]] && hint="y/N"
  while true; do
    local reply
    read -r -p "${msg} (${hint}): " reply || true
    if [[ -z "$reply" ]]; then
      reply="$default"
    fi
    case "$(echo "$reply" | tr '[:upper:]' '[:lower:]')" in
      y|yes) printf -v "$var" true; break ;;
      n|no) printf -v "$var" false; break ;;
      *) echo "请输入 y 或 n。" >&2 ;;
    esac
  done
}

read_secret() {
  local var="$1" msg="$2"
  local reply
  read -r -s -p "$msg" reply || true
  echo
  printf -v "$var" '%s' "$reply"
}

interactive_wizard() {
  if [[ "$DRY_RUN" == true ]]; then
    echo "==> dry-run：跳过交互向导，使用当前环境变量与默认值"
    return 0
  fi
  echo
  echo "======== 麒麟智训 安装向导 ========"
  echo

  if [[ "$CLI_PREFIX" != true ]]; then
    read_line "安装目录（INSTALL_PREFIX）" INSTALL_PREFIX
  fi

  if [[ "$CLI_SKIP_BUILD" != true ]]; then
    local _sb=false
    read_yesno _sb n "是否跳过编译（使用已有 WebBE/target 与 WebFE/target）"
    if [[ "$_sb" == true ]]; then
      SKIP_BUILD=true
    else
      SKIP_BUILD=false
    fi
  fi

  # 是否通过包管理器自动安装缺失依赖（与 --install-deps / --skip-install-deps 互斥）
  if [[ "$CLI_SKIP_INSTALL_DEPS" != true ]] && [[ "$CLI_AUTO_INSTALL_DEPS" != true ]]; then
    local _deps_def=y
    [[ "$SKIP_INSTALL_DEPS" == true ]] && _deps_def=n
    local _auto_sysdeps=false
    read_yesno _auto_sysdeps "$_deps_def" "是否自动安装缺失的系统依赖（JDK/Maven/Node、Python 与 AI 相关库、mysql 客户端、nginx 等，需 root 与联网）"
    if [[ "$_auto_sysdeps" == true ]]; then
      SKIP_INSTALL_DEPS=false
    else
      SKIP_INSTALL_DEPS=true
    fi
  elif [[ "$CLI_AUTO_INSTALL_DEPS" == true ]]; then
    SKIP_INSTALL_DEPS=false
  fi

  if [[ "$CLI_NGINX" != true ]]; then
    read_yesno WITH_NGINX n "是否写入 nginx 站点配置（/etc/nginx/conf.d/linhang.conf）"
  fi

  if [[ "$CLI_INFRA" != true ]]; then
    read_yesno WITH_INFRA y "是否自动安装并配置基础设施（MySQL + Redis）"
  fi

  if [[ "$CLI_INIT_DB" != true ]]; then
    echo
    echo "数据库：将执行 Base/init.sql（含 DROP TABLE，会清空该库中已有表）。"
    read_yesno INIT_DB n "是否初始化 MySQL 数据库"
  fi

  if [[ "$CLI_AI_AGENT" != true ]]; then
    read_yesno WITH_AI_AGENT n "是否部署 AI_AGENT 服务（Python 虚拟环境 + systemd）"
  fi
  if [[ "$WITH_AI_AGENT" == true ]] && [[ "$CLI_AI_AGENT_PORT" != true ]]; then
    read_line "AI_AGENT 端口（AI_AGENT_PORT）" AI_AGENT_PORT
    AI_AGENT_PORT="${AI_AGENT_PORT:-8000}"
  fi

  if [[ "$CLI_AUTO_START" != true ]]; then
    local _ast=true
    read_yesno _ast y "安装结束后是否自动启动服务（后端/AI_AGENT/nginx）"
    AUTO_START_SERVICES="$_ast"
  fi

  if [[ "$INIT_DB" == true ]]; then
    echo
    read_line "MySQL 主机（MYSQL_HOST）" MYSQL_HOST
    MYSQL_HOST="${MYSQL_HOST:-127.0.0.1}"
    read_line "MySQL 端口（MYSQL_PORT）" MYSQL_PORT
    MYSQL_PORT="${MYSQL_PORT:-3306}"
    read_line "数据库名（MYSQL_DATABASE）" MYSQL_DATABASE
    MYSQL_DATABASE="${MYSQL_DATABASE:-wdd}"
    read_line "管理员账号（MYSQL_ADMIN_USER）" MYSQL_ADMIN_USER
    MYSQL_ADMIN_USER="${MYSQL_ADMIN_USER:-root}"
    read_secret MYSQL_ADMIN_PASSWORD "管理员密码（MYSQL_ADMIN_PASSWORD，回车表示无密码）: "
    echo
    local _app=false
    read_yesno _app n "是否创建并授权应用账号（供后端连接，可选）"
    if [[ "$_app" == true ]]; then
      read_line "应用数据库用户名（MYSQL_APP_USER）" MYSQL_APP_USER
      read_secret MYSQL_APP_PASSWORD "应用数据库密码（MYSQL_APP_PASSWORD）: "
      echo
    else
      MYSQL_APP_USER=""
      MYSQL_APP_PASSWORD=""
    fi
  fi

  echo
  echo "-------- 汇总 --------"
  echo "  安装目录:     ${INSTALL_PREFIX}"
  echo "  跳过编译:     ${SKIP_BUILD}"
  echo "  初始化数据库: ${INIT_DB}"
  if [[ "$INIT_DB" == true ]]; then
    echo "  MySQL:        ${MYSQL_HOST:-127.0.0.1}:${MYSQL_PORT:-3306} 库=${MYSQL_DATABASE:-wdd} 用户=${MYSQL_ADMIN_USER:-root}"
    if [[ -n "${MYSQL_APP_USER:-}" ]]; then
      echo "  应用账号:     ${MYSQL_APP_USER}"
    fi
  fi
  echo "  nginx 配置:   ${WITH_NGINX}"
  echo "  基础设施:     ${WITH_INFRA}（MySQL + Redis）"
  echo "  AI_AGENT:     ${WITH_AI_AGENT}"
  if [[ "$WITH_AI_AGENT" == true ]]; then
    echo "  AI_AGENT端口: ${AI_AGENT_PORT}"
  fi
  if [[ "$SKIP_INSTALL_DEPS" == true ]]; then
    echo "  自动安装依赖: 否（已跳过）"
  else
    echo "  自动安装依赖: 是（缺失时通过包管理器安装）"
  fi
  echo "  自动启动服务: ${AUTO_START_SERVICES}"
  echo "  dry-run:      ${DRY_RUN}"
  echo "----------------------"
  local _go=n
  read_yesno _go y "确认按上述选项执行安装"
  if [[ "$_go" != true ]]; then
    echo "已取消。"
    exit 0
  fi
  echo
}

finalize_install_prefix() {
  if [[ "${INSTALL_PREFIX}" != /* ]]; then
    echo "错误: INSTALL_PREFIX 必须为绝对路径，当前: ${INSTALL_PREFIX}" >&2
    exit 1
  fi
  if [[ "$DRY_RUN" == true ]]; then
    return 0
  fi
  if command -v realpath >/dev/null 2>&1; then
    INSTALL_PREFIX="$(realpath -m "$INSTALL_PREFIX")"
  fi
}

# 将模板中的 @@INSTALL_ROOT@@ 替换为 INSTALL_PREFIX（路径可含特殊字符，优先 python3）
substitute_install_root() {
  local src="$1" dst="$2"
  if [[ ! -f "$src" ]]; then
    echo "错误: 找不到模板 $src" >&2
    exit 1
  fi
  if command -v python3 >/dev/null 2>&1; then
    INSTALL_PREFIX="$INSTALL_PREFIX" python3 -c "
import os, sys, pathlib
src = pathlib.Path(sys.argv[1])
dst = pathlib.Path(sys.argv[2])
path = os.environ['INSTALL_PREFIX']
dst.write_text(src.read_text(encoding='utf-8').replace('@@INSTALL_ROOT@@', path), encoding='utf-8')
" "$src" "$dst"
  else
    local esc
    esc=$(printf '%s' "$INSTALL_PREFIX" | sed -e 's/[\/&|]/\\&/g')
    sed -e "s|@@INSTALL_ROOT@@|${esc}|g" "$src" >"$dst"
  fi
}

validate_ai_agent_port() {
  if [[ "$WITH_AI_AGENT" != true ]]; then
    return 0
  fi
  if [[ ! "${AI_AGENT_PORT}" =~ ^[0-9]+$ ]] || (( AI_AGENT_PORT < 1 || AI_AGENT_PORT > 65535 )); then
    echo "错误: AI_AGENT_PORT 必须是 1-65535 的整数，当前: ${AI_AGENT_PORT}" >&2
    exit 1
  fi
}

require_root_for_install() {
  if [[ "$DRY_RUN" == true ]]; then
    return 0
  fi
  if [[ "${EUID:-0}" -ne 0 ]]; then
    echo "安装到 ${INSTALL_PREFIX} 并注册 systemd 需要 root，请使用: sudo $0 ..." >&2
    exit 1
  fi
}

# 与 Base/install-linux-deps.sh 一致，用于自动安装系统包
detect_pkg_mgr_for_install() {
  if command -v apt-get >/dev/null 2>&1; then echo apt
  elif command -v dnf >/dev/null 2>&1; then echo dnf
  elif command -v yum >/dev/null 2>&1; then echo yum
  elif command -v zypper >/dev/null 2>&1; then echo zypper
  else
    echo "错误: 未检测到 apt/dnf/yum/zypper，无法自动安装依赖。请手动安装后重试，或使用 --skip-install-deps。" >&2
    return 1
  fi
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
    echo redis-server
  fi
}

redis_conf_path() {
  for p in /etc/redis/redis.conf /etc/redis.conf; do
    if [[ -f "$p" ]]; then
      echo "$p"
      return 0
    fi
  done
  return 1
}

# 按当前选项安装缺失的系统依赖（需 root；可用 SKIP_INSTALL_DEPS=1 或 --skip-install-deps 关闭）
auto_install_missing_deps() {
  if [[ "$SKIP_INSTALL_DEPS" == true ]]; then
    echo "==> 已跳过自动安装系统依赖（SKIP_INSTALL_DEPS / --skip-install-deps）"
    return 0
  fi
  if [[ "$DRY_RUN" == true ]]; then
    echo "==> dry-run：将按需自动安装系统依赖（可用 --skip-install-deps 关闭）"
    return 0
  fi
  local pm
  pm="$(detect_pkg_mgr_for_install)" || return 1
  echo "==> 包管理器: ${pm}（自动补齐缺失依赖）"

  if [[ "$SKIP_BUILD" != true ]]; then
    local need=false
    for c in java mvn node npm; do
      command -v "$c" >/dev/null 2>&1 || need=true
    done
    if [[ "$need" == true ]]; then
      echo "==> 安装编译依赖: JDK、Maven、Node.js、npm"
      case "${pm}" in
        apt)
          export DEBIAN_FRONTEND=noninteractive
          apt-get update -y
          apt-get install -y openjdk-17-jdk-headless maven nodejs npm \
            || apt-get install -y default-jdk-headless maven nodejs npm
          ;;
        dnf|yum)
          "${pm}" install -y java-17-openjdk-devel maven nodejs npm \
            || "${pm}" install -y java-11-openjdk-devel maven nodejs npm
          ;;
        zypper)
          zypper --non-interactive refresh
          zypper --non-interactive install -y java-17-openjdk-devel maven nodejs npm \
            || zypper --non-interactive install -y java-11-openjdk-devel maven nodejs npm
          ;;
      esac
    fi
  fi

  if [[ "$WITH_AI_AGENT" == true ]]; then
    if ! command -v python3 >/dev/null 2>&1; then
      echo "==> 安装 Python 3"
      case "${pm}" in
        apt)
          export DEBIAN_FRONTEND=noninteractive
          apt-get install -y python3
          ;;
        dnf|yum) "${pm}" install -y python3 ;;
        zypper) zypper --non-interactive install -y python3 ;;
      esac
    fi
    echo "==> 安装 AI_AGENT 相关: venv、pip、C 编译与 dbus-python / PyGObject 等常见系统库"
    case "${pm}" in
      apt)
        export DEBIAN_FRONTEND=noninteractive
        apt-get install -y \
          python3-venv python3-pip python3-dev \
          build-essential pkg-config meson ninja-build \
          libdbus-1-dev libglib2.0-dev libgirepository1.0-dev libcairo2-dev
        ;;
      dnf|yum)
        "${pm}" install -y python3-devel python3-pip gcc gcc-c++ make \
          pkgconf-pkg-config meson ninja-build \
          dbus-devel glib2-devel gobject-introspection-devel cairo-devel \
          redhat-rpm-config \
          || echo "警告: 部分 AI_AGENT 系统库安装失败，pip 安装可能仍需手动补包。" >&2
        ;;
      zypper)
        zypper --non-interactive install -y python3-devel python3-pip \
          gcc gcc-c++ meson ninja pkg-config \
          dbus-1-devel glib2-devel gobject-introspection-devel cairo-devel \
          || echo "警告: 部分 AI_AGENT 系统库安装失败，pip 安装可能仍需手动补包。" >&2
        ;;
    esac
  fi

  if [[ "$INIT_DB" == true ]]; then
    if ! command -v mysql >/dev/null 2>&1; then
      echo "==> 安装 MySQL/MariaDB 客户端（mysql）"
      case "${pm}" in
        apt)
          export DEBIAN_FRONTEND=noninteractive
          apt-get install -y default-mysql-client \
            || apt-get install -y mariadb-client
          ;;
        dnf|yum)
          "${pm}" install -y mysql \
            || "${pm}" install -y mariadb
          ;;
        zypper)
          zypper --non-interactive install -y mariadb-client
          ;;
      esac
    fi
  fi

  if [[ "$WITH_INFRA" == true ]]; then
    echo "==> 安装基础设施服务: MySQL Server + Redis Server"
    case "${pm}" in
      apt)
        export DEBIAN_FRONTEND=noninteractive
        apt-get install -y mysql-server redis-server \
          || apt-get install -y mariadb-server redis-server
        ;;
      dnf|yum)
        "${pm}" install -y mysql-server redis \
          || "${pm}" install -y mariadb-server redis
        ;;
      zypper)
        zypper --non-interactive install -y mysql redis \
          || zypper --non-interactive install -y mariadb redis
        ;;
    esac
  fi

  if [[ "$WITH_NGINX" == true ]]; then
    if ! command -v nginx >/dev/null 2>&1; then
      echo "==> 安装 nginx"
      case "${pm}" in
        apt)
          export DEBIAN_FRONTEND=noninteractive
          apt-get install -y nginx
          ;;
        dnf|yum) "${pm}" install -y nginx ;;
        zypper) zypper --non-interactive install -y nginx ;;
      esac
    fi
  fi
}

configure_and_start_infra() {
  if [[ "$WITH_INFRA" != true ]]; then
    return 0
  fi
  if [[ "$DRY_RUN" == true ]]; then
    echo "==> dry-run：将启动并配置 MySQL/Redis（包含 Redis 密码与端口）"
    return 0
  fi

  local msvc rsvc rconf rp
  msvc="$(mysql_service_name)"
  rsvc="$(redis_service_name)"

  echo "==> 启动并启用数据库服务: ${msvc}"
  systemctl enable "${msvc}" 2>/dev/null || true
  systemctl restart "${msvc}"
  systemctl is-active --quiet "${msvc}" || {
    echo "错误: 服务 ${msvc} 未运行" >&2
    exit 1
  }

  if rconf="$(redis_conf_path)"; then
    rp="${REDIS_PASSWORD//\'/\'\\\'\'}"
    if grep -qE '^[[:space:]]*requirepass[[:space:]]+' "${rconf}"; then
      sed -i -E "s/^[[:space:]]*requirepass[[:space:]].*/requirepass ${rp}/" "${rconf}"
    elif grep -qE '^# requirepass' "${rconf}"; then
      sed -i -E "s/^# requirepass.*/requirepass ${rp}/" "${rconf}"
    else
      printf '\nrequirepass %s\n' "${rp}" >> "${rconf}"
    fi
    if grep -qE '^[[:space:]]*port[[:space:]]+' "${rconf}"; then
      sed -i -E "s/^[[:space:]]*port[[:space:]].*/port ${REDIS_PORT}/" "${rconf}"
    elif grep -qE '^# port' "${rconf}"; then
      sed -i -E "s/^# port.*/port ${REDIS_PORT}/" "${rconf}"
    else
      printf '\nport %s\n' "${REDIS_PORT}" >> "${rconf}"
    fi
  else
    echo "警告: 未找到 redis.conf，跳过密码与端口写入。" >&2
  fi

  echo "==> 启动并启用 Redis 服务: ${rsvc}"
  systemctl enable "${rsvc}" 2>/dev/null || true
  systemctl restart "${rsvc}"
  systemctl is-active --quiet "${rsvc}" || {
    echo "错误: 服务 ${rsvc} 未运行" >&2
    exit 1
  }
}

check_build_deps() {
  for cmd in java mvn node npm; do
    if ! command -v "$cmd" >/dev/null 2>&1; then
      echo "错误: 未找到「$cmd」，请先安装 JDK 8+、Maven、Node.js 16+。" >&2
      exit 1
    fi
  done
}

check_ai_agent_deps() {
  for cmd in python3; do
    if ! command -v "$cmd" >/dev/null 2>&1; then
      echo "错误: 未找到「$cmd」，部署 AI_AGENT 需要 Python 3.8+。" >&2
      exit 1
    fi
  done
}

do_build() {
  if [[ "$SKIP_BUILD" == true ]]; then
    echo "==> 跳过编译"
    return 0
  fi
  if [[ "$DRY_RUN" == true ]]; then
    echo "==> dry-run：将执行 WebBE/build.sh 与 WebFE/build.sh（不校验 JDK/Maven/Node）"
    run bash "${REPO_ROOT}/WebBE/build.sh"
    run bash "${REPO_ROOT}/WebFE/build.sh"
    return 0
  fi
  check_build_deps
  echo "==> 编译 WebBE"
  run bash "${REPO_ROOT}/WebBE/build.sh"
  echo "==> 编译 WebFE"
  run bash "${REPO_ROOT}/WebFE/build.sh"
}

assert_artifacts() {
  if [[ "$DRY_RUN" == true ]]; then
    echo "==> dry-run：跳过产物校验"
    return 0
  fi
  local be="${REPO_ROOT}/WebBE/target"
  local fe="${REPO_ROOT}/WebFE/target"
  local f
  for f in "$be/ueit-admin.jar" "$be/ueit-user-web.jar" "$be/ueit-user-mobile.jar" "$be/start.sh"; do
    if [[ ! -f "$f" ]]; then
      echo "错误: 缺少 $f，请先执行编译或不要跳过编译。" >&2
      exit 1
    fi
  done
  for f in "$fe/ueit-user-web" "$fe/ueit-admin"; do
    if [[ ! -d "$f" ]]; then
      echo "错误: 缺少目录 $f，请先执行 WebFE 编译。" >&2
      exit 1
    fi
  done
}

install_tree() {
  local root="$1"
  echo "==> 安装文件到 ${root}"
  run install -d -m 755 "${root}/be" "${root}/web" "${root}/resource/web-file" "${root}/resource/servlet" "${root}/resource/stop-word"
  if [[ "$DRY_RUN" == true ]]; then
    echo "[dry-run] 将复制 WebBE/target/*.jar、start.sh 与 WebFE/target 下静态目录"
    return 0
  fi
  shopt -s nullglob
  local jars=( "${REPO_ROOT}/WebBE/target/"*.jar )
  shopt -u nullglob
  if [[ ${#jars[@]} -eq 0 ]]; then
    echo "错误: ${REPO_ROOT}/WebBE/target 下未找到 jar" >&2
    exit 1
  fi
  install -m 644 "${jars[@]}" "${root}/be/"
  install -m 755 "${REPO_ROOT}/WebBE/target/start.sh" "${root}/be/start.sh"
  cp -a "${REPO_ROOT}/WebFE/target/ueit-user-web" "${root}/web/"
  cp -a "${REPO_ROOT}/WebFE/target/ueit-admin" "${root}/web/"
  {
    printf '%s\n' "麒麟智训后端安装目录：${root}/be" "静态资源：${root}/web/{ueit-user-web,ueit-admin}" "上传目录（需与后端配置一致）：${root}/resource/web-file"
  } > "${root}/be/README.txt"
  chmod 644 "${root}/be/README.txt"
}

install_ai_agent_tree() {
  local src="${REPO_ROOT}/AI_AGENT/projects"
  local dst="${INSTALL_PREFIX}/ai-agent"
  if [[ ! -d "$src" ]]; then
    echo "错误: 未找到 AI_AGENT 项目目录 $src" >&2
    exit 1
  fi
  echo "==> 安装 AI_AGENT 到 ${dst}"
  run install -d -m 755 "${dst}"
  if [[ "$DRY_RUN" == true ]]; then
    echo "[dry-run] 将复制 AI_AGENT/projects 到 ${dst}"
    return 0
  fi
  cp -a "${src}/." "${dst}/"
  install -d -m 755 "${dst}/logs"
  if [[ ! -f "${dst}/.env" ]] && [[ -f "${dst}/.env.example" ]]; then
    cp -a "${dst}/.env.example" "${dst}/.env"
  fi
}

setup_ai_agent_venv() {
  local dst="${INSTALL_PREFIX}/ai-agent"
  local py="${dst}/.venv/bin/python"
  local req="${dst}/requirements.txt"
  if [[ ! -f "$req" ]]; then
    echo "错误: 缺少 AI_AGENT 依赖文件 $req" >&2
    exit 1
  fi
  echo "==> 创建 AI_AGENT Python 虚拟环境"
  run python3 -m venv "${dst}/.venv"
  if [[ "$DRY_RUN" == true ]]; then
    echo "[dry-run] 将安装 AI_AGENT Python 依赖"
    return 0
  fi
  "$py" -m pip install --upgrade pip
  "$py" -m pip install -r "$req"
}

init_database() {
  if [[ "$INIT_DB" != true ]]; then
    return 0
  fi
  if [[ "$DRY_RUN" == true ]]; then
    echo "==> dry-run：将执行 MySQL 初始化（Base/init.sql → 库 ${MYSQL_DATABASE:-wdd}）"
    return 0
  fi
  local sql="${REPO_ROOT}/Base/init.sql"
  if [[ ! -f "$sql" ]]; then
    echo "错误: 未找到 $sql" >&2
    exit 1
  fi
  if ! command -v mysql >/dev/null 2>&1; then
    echo "错误: 未找到 mysql 客户端，请安装 mysql-client / mariadb-client。" >&2
    exit 1
  fi
  local host="${MYSQL_HOST:-127.0.0.1}"
  local port="${MYSQL_PORT:-3306}"
  local db="${MYSQL_DATABASE:-wdd}"
  local admin="${MYSQL_ADMIN_USER:-root}"
  local pass="${MYSQL_ADMIN_PASSWORD:-}"
  local app_user="${MYSQL_APP_USER:-$db}"
  local app_pass="${MYSQL_APP_PASSWORD:-wdd123}"
  echo "==> 初始化数据库 ${host}:${port}/${db}（init.sql 含 DROP TABLE，将覆盖已有表）"
  local args=( -h"$host" -P"$port" -u"$admin" )
  if [[ -n "$pass" ]]; then
    args+=( -p"$pass" )
  fi
  mysql "${args[@]}" -e "CREATE DATABASE IF NOT EXISTS \`${db}\` CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;"
  mysql "${args[@]}" "$db" <"$sql"
  echo "==> 已导入 $sql"
  if [[ -n "${app_user}" ]] && [[ -n "${app_pass}" ]]; then
    echo "==> 创建/授权应用账号 ${app_user}"
    mysql "${args[@]}" -e "
CREATE USER IF NOT EXISTS '${app_user}'@'%' IDENTIFIED BY '${app_pass}';
CREATE USER IF NOT EXISTS '${app_user}'@'127.0.0.1' IDENTIFIED BY '${app_pass}';
GRANT ALL PRIVILEGES ON \`${db}\`.* TO '${app_user}'@'%';
GRANT ALL PRIVILEGES ON \`${db}\`.* TO '${app_user}'@'127.0.0.1';
FLUSH PRIVILEGES;
"
  fi
}

ensure_user() {
  if [[ "$DRY_RUN" == true ]]; then
    echo "[dry-run] 若不存在则创建系统用户 ${RUN_USER}（无登录 shell）"
    return 0
  fi
  if ! id -u "$RUN_USER" &>/dev/null; then
    useradd -r -M -s /usr/sbin/nologin "$RUN_USER"
    echo "==> 已创建系统用户 ${RUN_USER}"
  fi
}

chown_install() {
  run chown -R "${RUN_USER}:${RUN_USER}" "${INSTALL_PREFIX}"
}

install_systemd_unit() {
  local unit="/etc/systemd/system/${SERVICE_NAME}.service"
  if [[ ! -f "${DEPLOY_TPL}/linhang.service.in" ]]; then
    echo "错误: 缺少模板 ${DEPLOY_TPL}/linhang.service.in" >&2
    exit 1
  fi
  echo "==> 写入 systemd 单元 ${unit}"
  if [[ "$DRY_RUN" == true ]]; then
    echo "[dry-run] sed 模板 -> ${unit}"
    return 0
  fi
  sed -e "s|@@INSTALL_ROOT@@|${INSTALL_PREFIX}|g" -e "s|@@RUN_USER@@|${RUN_USER}|g" "${DEPLOY_TPL}/linhang.service.in" >"$unit"
  chmod 644 "$unit"
  systemctl daemon-reload
  systemctl enable "${SERVICE_NAME}.service"
  echo "==> 已 enable ${SERVICE_NAME}.service；启动请执行: sudo systemctl start ${SERVICE_NAME}"
}

install_ai_agent_systemd_unit() {
  local unit="/etc/systemd/system/${SERVICE_NAME}-ai-agent.service"
  local tpl="${DEPLOY_TPL}/ai-agent.service.in"
  if [[ ! -f "$tpl" ]]; then
    echo "错误: 缺少模板 $tpl" >&2
    exit 1
  fi
  echo "==> 写入 AI_AGENT systemd 单元 ${unit}"
  if [[ "$DRY_RUN" == true ]]; then
    echo "[dry-run] sed 模板 -> ${unit}"
    return 0
  fi
  sed \
    -e "s|@@INSTALL_ROOT@@|${INSTALL_PREFIX}|g" \
    -e "s|@@RUN_USER@@|${RUN_USER}|g" \
    -e "s|@@AI_AGENT_PORT@@|${AI_AGENT_PORT}|g" \
    "$tpl" >"$unit"
  chmod 644 "$unit"
  systemctl daemon-reload
  systemctl enable "${SERVICE_NAME}-ai-agent.service"
  echo "==> 已 enable ${SERVICE_NAME}-ai-agent.service；启动请执行: sudo systemctl start ${SERVICE_NAME}-ai-agent"
}

install_nginx_conf() {
  local dst="/etc/nginx/conf.d/linhang.conf"
  if [[ ! -f "${DEPLOY_TPL}/nginx-linhang.conf.in" ]]; then
    echo "错误: 缺少模板 ${DEPLOY_TPL}/nginx-linhang.conf.in" >&2
    exit 1
  fi
  echo "==> 写入 nginx 配置 ${dst}（前端/上传路径基于 INSTALL_PREFIX=${INSTALL_PREFIX}）"
  if [[ "$DRY_RUN" == true ]]; then
    echo "[dry-run] 替换 @@INSTALL_ROOT@@ -> ${INSTALL_PREFIX} -> ${dst}"
    return 0
  fi
  substitute_install_root "${DEPLOY_TPL}/nginx-linhang.conf.in" "$dst"
  chmod 644 "$dst"
  if command -v nginx >/dev/null 2>&1; then
    if nginx -t 2>/dev/null; then
      systemctl reload nginx 2>/dev/null || true
    else
      echo "警告: nginx -t 未通过，请检查主配置与站点冲突后手动 reload。" >&2
    fi
  else
    echo "未检测到 nginx 命令，已仅写入配置文件。" >&2
  fi
}

start_deployed_services() {
  if [[ "$AUTO_START_SERVICES" != true ]]; then
    echo "==> 已跳过自动启动服务（--no-start-services）"
    return 0
  fi
  if [[ "$DRY_RUN" == true ]]; then
    echo "==> dry-run：将自动启动后端/AI_AGENT/nginx 服务"
    return 0
  fi

  echo "==> 启动后端服务 ${SERVICE_NAME}.service"
  systemctl restart "${SERVICE_NAME}.service"
  systemctl is-active --quiet "${SERVICE_NAME}.service" || {
    echo "错误: ${SERVICE_NAME}.service 启动失败，请查看: systemctl status ${SERVICE_NAME}.service" >&2
    exit 1
  }

  if [[ "$WITH_AI_AGENT" == true ]]; then
    echo "==> 启动 AI_AGENT 服务 ${SERVICE_NAME}-ai-agent.service"
    systemctl restart "${SERVICE_NAME}-ai-agent.service"
    systemctl is-active --quiet "${SERVICE_NAME}-ai-agent.service" || {
      echo "错误: ${SERVICE_NAME}-ai-agent.service 启动失败，请查看: systemctl status ${SERVICE_NAME}-ai-agent.service" >&2
      exit 1
    }
  fi

  if [[ "$WITH_NGINX" == true ]]; then
    if command -v nginx >/dev/null 2>&1; then
      echo "==> 重启 nginx"
      systemctl restart nginx
      systemctl is-active --quiet nginx || {
        echo "错误: nginx 启动失败，请查看: systemctl status nginx" >&2
        exit 1
      }
    fi
  fi
}

main() {
  if [[ "$NON_INTERACTIVE" != true ]]; then
    interactive_wizard
  fi

  finalize_install_prefix
  validate_ai_agent_port
  require_root_for_install
  auto_install_missing_deps
  configure_and_start_infra
  do_build
  assert_artifacts
  init_database
  install_tree "$INSTALL_PREFIX"
  if [[ "$WITH_AI_AGENT" == true ]]; then
    check_ai_agent_deps
    install_ai_agent_tree
    setup_ai_agent_venv
  fi
  ensure_user
  chown_install
  install_systemd_unit
  if [[ "$WITH_AI_AGENT" == true ]]; then
    install_ai_agent_systemd_unit
  fi
  if [[ "$WITH_NGINX" == true ]]; then
    install_nginx_conf
  fi
  start_deployed_services
  echo
  echo "安装完成。"
  echo "  数据目录: ${INSTALL_PREFIX}"
  echo "  服务名:   ${SERVICE_NAME}.service"
  echo "  启动:     sudo systemctl start ${SERVICE_NAME}"
  echo "  状态:     sudo systemctl status ${SERVICE_NAME}"
  if [[ "$WITH_INFRA" == true ]]; then
    echo "  MySQL:    127.0.0.1:3306（初始化库时默认应用账号: ${MYSQL_APP_USER:-${MYSQL_DATABASE:-wdd}}）"
    echo "  Redis:    127.0.0.1:${REDIS_PORT}（密码: ${REDIS_PASSWORD}）"
  fi
  if [[ "$WITH_AI_AGENT" == true ]]; then
    echo "  AI_AGENT: sudo systemctl start ${SERVICE_NAME}-ai-agent"
    echo "            sudo systemctl status ${SERVICE_NAME}-ai-agent"
    echo "            本地测试: curl http://127.0.0.1:${AI_AGENT_PORT}/health"
  fi
  if [[ "$WITH_NGINX" == true ]]; then
    echo "  nginx:    /etc/nginx/conf.d/linhang.conf（@@INSTALL_ROOT@@ → ${INSTALL_PREFIX}；前端 web/、上传 resource/web-file）"
  fi
  if [[ "$WITH_NGINX" != true ]]; then
    echo "  提示: 需要 nginx 时可重新运行本脚本并选择写入 nginx，或: sudo INSTALL_PREFIX=${INSTALL_PREFIX} $0 -y --with-nginx --skip-build"
  fi
  if [[ "$WITH_AI_AGENT" != true ]]; then
    echo "  提示: 需要 AI_AGENT 时可执行: sudo INSTALL_PREFIX=${INSTALL_PREFIX} $0 -y --with-ai-agent --skip-build"
  fi
  if [[ "$INIT_DB" != true ]]; then
    echo "  数据库: 若尚未导入，可重新运行本脚本并选择初始化数据库，或: sudo $0 -y --skip-build --init-db"
  fi
}

main
