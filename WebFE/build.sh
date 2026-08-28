#!/usr/bin/env bash
# WebFE：在 Linux / WSL 下对子项目执行 npm 安装与 Vite 生产构建，并将静态产物汇总到 target/。
# 用法：
#   ./build.sh                       # 默认：npm install + 全部子项目 build
#   SKIP_INSTALL=true ./build.sh     # 跳过安装（node_modules 已就绪时加快重复构建）
#   ONLY=wdd-admin ./build.sh        # 只构建指定子目录（wdd-user-web | wdd-admin）
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

for cmd in node npm; do
  if ! command -v "$cmd" >/dev/null 2>&1; then
    echo "错误: 未找到命令「$cmd」。请安装 Node.js 16+（含 npm），并确保在 PATH 中。" >&2
    exit 1
  fi
done

NODE_MAJOR=$(node -p "parseInt(process.versions.node.split('.')[0], 10)")
if [[ "$NODE_MAJOR" -lt 16 ]]; then
  echo "错误: 需要 Node.js >= 16，当前: $(node -p process.version)" >&2
  exit 1
fi

echo "==> Node / npm"
node -v
npm -v

SKIP_INSTALL="${SKIP_INSTALL:-false}"
ONLY="${ONLY:-}"

should_build() {
  local name="$1"
  if [[ -z "$ONLY" ]]; then
    return 0
  fi
  [[ "$ONLY" == "$name" ]]
}

# 子项目目录 | Vite 输出目录名（相对子项目）| 复制到 target/ 下的目录名
declare -a ROWS=(
  "wdd-user-web|ueit-user-web|ueit-user-web"
  "wdd-admin|ueit-admin|ueit-admin"
)

OUT_ROOT="${SCRIPT_DIR}/target"
rm -rf "$OUT_ROOT"
mkdir -p "$OUT_ROOT"

for row in "${ROWS[@]}"; do
  IFS='|' read -r subdir build_out target_name <<<"$row"
  if ! should_build "$subdir"; then
    echo "==> 跳过 $subdir（ONLY=$ONLY）"
    continue
  fi

  APP_DIR="${SCRIPT_DIR}/${subdir}"
  if [[ ! -f "${APP_DIR}/package.json" ]]; then
    echo "错误: 未找到 ${APP_DIR}/package.json" >&2
    exit 1
  fi

  echo "==> ${subdir}"
  cd "$APP_DIR"

  if [[ "$SKIP_INSTALL" != "true" ]]; then
    npm install
  fi

  npm run build

  BUILT="${APP_DIR}/${build_out}"
  if [[ ! -d "$BUILT" ]]; then
    echo "错误: 构建后未找到目录 $BUILT" >&2
    exit 1
  fi

  DEST="${OUT_ROOT}/${target_name}"
  mkdir -p "$(dirname "$DEST")"
  cp -a "$BUILT" "$DEST"
done

echo
echo "构建完成。"
echo "静态资源目录: ${OUT_ROOT}/"
ls -la "$OUT_ROOT" 2>/dev/null || true
echo "部署: 将 ${OUT_ROOT}/ 下 ueit-user-web、ueit-admin 按 Nginx 等配置指向站点根或子路径。"
