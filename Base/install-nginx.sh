#!/usr/bin/env bash
# 在 Linux 上安装 Nginx，并将 Nginx/nginx.conf.in 中的 @@INSTALL_ROOT@@ 替换为安装根目录后写入主配置。
# 目录约定与 install.sh 一致：INSTALL_PREFIX/web/{ueit-user-web,ueit-admin}、INSTALL_PREFIX/resource/web-file
# 需要 root。用法: sudo INSTALL_PREFIX=/opt/linhang ./install-nginx.sh
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ENV_FILE="${SCRIPT_DIR}/deps.env"
if [[ -f "${ENV_FILE}" ]]; then
  # shellcheck source=/dev/null
  source "${ENV_FILE}"
fi

INSTALL_PREFIX="${INSTALL_PREFIX:-/opt/linhang}"
NGINX_TEMPLATE="${NGINX_CONF_SOURCE:-${SCRIPT_DIR}/../Nginx/nginx.conf.in}"
NGINX_CONF_TARGET="${NGINX_CONF_TARGET:-/etc/nginx/nginx.conf}"
UPLOAD_ROOT="${UPLOAD_ROOT:-${INSTALL_PREFIX}/resource/web-file}"
USER_WEB_ROOT="${USER_WEB_ROOT:-${INSTALL_PREFIX}/web/ueit-user-web}"
ADMIN_WEB_ROOT="${ADMIN_WEB_ROOT:-${INSTALL_PREFIX}/web/ueit-admin}"
SKIP_RELOAD="${SKIP_RELOAD:-0}"

log() { echo "[install-nginx] $*"; }
die() { echo "[install-nginx] 错误: $*" >&2; exit 1; }

if [[ "$(id -u)" -ne 0 ]]; then
  die "请使用 root 运行，例如: sudo $0"
fi

if [[ "${INSTALL_PREFIX}" != /* ]]; then
  die "INSTALL_PREFIX 须为绝对路径，当前: ${INSTALL_PREFIX}"
fi
if command -v realpath >/dev/null 2>&1; then
  INSTALL_PREFIX="$(realpath -m "$INSTALL_PREFIX")"
fi

substitute_install_root() {
  local src="$1" dst="$2"
  [[ -f "$src" ]] || die "找不到模板: $src"
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

detect_pkg_mgr() {
  if command -v apt-get >/dev/null 2>&1; then echo apt
  elif command -v dnf >/dev/null 2>&1; then echo dnf
  elif command -v yum >/dev/null 2>&1; then echo yum
  elif command -v zypper >/dev/null 2>&1; then echo zypper
  else die "未检测到支持的包管理器 (apt/dnf/yum/zypper)"
  fi
}

PKG_MGR="$(detect_pkg_mgr)"

install_nginx_pkg() {
  case "${PKG_MGR}" in
    apt)
      export DEBIAN_FRONTEND=noninteractive
      apt-get update -y
      apt-get install -y nginx
      ;;
    dnf|yum)
      "${PKG_MGR}" install -y nginx
      ;;
    zypper)
      zypper --non-interactive install -y nginx
      ;;
  esac
}

nginx_service_name() {
  echo nginx
}

backup_existing_conf() {
  if [[ -f "${NGINX_CONF_TARGET}" ]]; then
    local bak
    bak="${NGINX_CONF_TARGET}.bak.$(date +%Y%m%d%H%M%S)"
    log "已存在 ${NGINX_CONF_TARGET}，备份为 ${bak}"
    cp -a "${NGINX_CONF_TARGET}" "${bak}"
  fi
}

deploy_conf() {
  [[ -f "${NGINX_TEMPLATE}" ]] || die "找不到 Nginx 配置模板: ${NGINX_TEMPLATE}"
  install -d -m 0755 "$(dirname "${NGINX_CONF_TARGET}")"
  log "根据 INSTALL_PREFIX=${INSTALL_PREFIX} 生成 ${NGINX_CONF_TARGET}"
  substitute_install_root "${NGINX_TEMPLATE}" "${NGINX_CONF_TARGET}"
  chmod 0644 "${NGINX_CONF_TARGET}"
}

chown_nginx_tree() {
  local d="$1"
  if id www-data >/dev/null 2>&1; then
    chown -R www-data:www-data "${d}" 2>/dev/null || true
  elif id nginx >/dev/null 2>&1; then
    chown -R nginx:nginx "${d}" 2>/dev/null || true
  fi
}

ensure_upload_dir() {
  install -d -m 0755 "${UPLOAD_ROOT}"
  chown_nginx_tree "${UPLOAD_ROOT}"
}

ensure_static_web_roots() {
  install -d -m 0755 "${USER_WEB_ROOT}" "${ADMIN_WEB_ROOT}"
  chown_nginx_tree "${USER_WEB_ROOT}"
  chown_nginx_tree "${ADMIN_WEB_ROOT}"
}

main() {
  log "包管理器: ${PKG_MGR}"
  log "安装 Nginx..."
  install_nginx_pkg

  backup_existing_conf
  deploy_conf
  log "创建上传目录: ${UPLOAD_ROOT}"
  ensure_upload_dir
  log "创建前端静态目录: ${USER_WEB_ROOT}、${ADMIN_WEB_ROOT}"
  ensure_static_web_roots

  nginx -t
  SVC="$(nginx_service_name)"
  systemctl enable "${SVC}" 2>/dev/null || true
  if [[ "${SKIP_RELOAD}" == "1" ]]; then
    log "已设置 SKIP_RELOAD=1，跳过 systemctl reload。"
  else
    systemctl restart "${SVC}"
    systemctl is-active --quiet "${SVC}" || die "服务 ${SVC} 未运行"
  fi

  log "完成。静态与上传根路径均相对于 INSTALL_PREFIX=${INSTALL_PREFIX}；/api/ 仍反代本机后端。"
}

main "$@"
