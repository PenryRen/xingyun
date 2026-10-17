#!/usr/bin/env bash
# 麟航智训 — 一键部署（Linux / WSL2）
#
# 作用：在非交互模式下调用 install.sh，默认开启「自动安装缺失的系统依赖」
#（JDK、Maven、Node/npm、以及按需的 MySQL 客户端、nginx、AI 相关库等），
# 随后执行 WebBE/WebFE 编译（Maven 与 npm 会从网络拉取依赖），并将产物安装到 INSTALL_PREFIX。
#
# 前置：root + 联网 + apt/dnf/yum/zypper 之一。WSL2 建议启用 systemd（/etc/wsl.conf 中 [boot] systemd=true）。
#
# 用法示例：
#   sudo ./one-click-deploy.sh
#   sudo ./one-click-deploy.sh --init-db
#   sudo INSTALL_PREFIX=/srv/linhang ./one-click-deploy.sh --with-nginx
#   sudo ./one-click-deploy.sh --no-infra --no-init-db
#   sudo ./one-click-deploy.sh --with-ai-agent --ai-agent-port 8000
#   sudo ./one-click-deploy.sh --dry-run
#
# 首次全新环境若要导入库表（会执行 Base/init.sql，含 DROP TABLE）：请加 --init-db。
# 其余参数与 install.sh 一致，会原样透传。
#
set -euo pipefail

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
INSTALL_SH="${REPO_ROOT}/install.sh"

os="$(uname -s 2>/dev/null || echo unknown)"
case "$os" in
  Linux*) ;;
  *)
    echo "错误: 当前系统为「${os}」，本脚本仅适用于 Linux 或 WSL2。" >&2
    echo "在 Windows 上请打开 WSL 发行版，进入仓库目录后执行: sudo ./one-click-deploy.sh" >&2
    exit 1
    ;;
esac

if [[ ! -f "$INSTALL_SH" ]]; then
  echo "错误: 未找到 ${INSTALL_SH}" >&2
  exit 1
fi

if [[ "${EUID:-0}" -ne 0 ]]; then
  echo "一键部署需要 root（安装软件包、写入安装目录、注册 systemd）。" >&2
  echo "请执行: sudo $0 $*" >&2
  exit 1
fi

# 非 TTY 时 install.sh 也会设 NON_INTERACTIVE；此处显式保证自动化流水线行为
export NON_INTERACTIVE="${NON_INTERACTIVE:-1}"

exec bash "$INSTALL_SH" -y --install-deps "$@"
