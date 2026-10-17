#!/bin/bash
set -e

# 项目根目录
WORK_DIR="${WORKSPACE_PATH:-.}"
PORT=8000
VENV_PY="${WORK_DIR}/.venv/bin/python"

usage() {
  echo "用法: $0 -p <端口>"
}

while getopts "p:h" opt; do
  case "$opt" in
    p)
      PORT="$OPTARG"
      ;;
    h)
      usage
      exit 0
      ;;
    \?)
      echo "无效选项: -$OPTARG"
      usage
      exit 1
      ;;
  esac
done

if [[ -x "$VENV_PY" ]]; then
  PYTHON="$VENV_PY"
else
  PYTHON="python3"
fi

cd "${WORK_DIR}/src" || exit 1
# 与 wdd-user-web 前端 /api/v1/chat 对齐，使用 server.api 应用（非 main.py 的 main:app）
exec "$PYTHON" -m uvicorn server.api:app --host 0.0.0.0 --port "$PORT"
