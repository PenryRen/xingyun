#!/usr/bin/env bash
# 在 build.sh 成功后，将 target/ 打成 tar.gz，便于上传部署。
# 用法: ./pack-dist.sh
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

if [[ ! -d "${SCRIPT_DIR}/target" ]] || ! ls -A "${SCRIPT_DIR}/target" >/dev/null 2>&1; then
  echo "未找到或非空的 target/，请先执行 ./build.sh" >&2
  exit 1
fi

VERSION="$(node -p "require('./wdd-admin/package.json').version" 2>/dev/null || echo "0.0.0")"
ARCHIVE="webfe-${VERSION}-linux-amd64.tar.gz"
tar -czvf "${SCRIPT_DIR}/${ARCHIVE}" -C "${SCRIPT_DIR}/target" .
echo "已生成: ${SCRIPT_DIR}/${ARCHIVE}"
