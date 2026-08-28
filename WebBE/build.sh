#!/usr/bin/env bash
# WebBE：在 Linux / WSL 下执行 Maven 编译打包。
# 用法：
#   ./build.sh              # 默认跳过单元测试（SKIP_TESTS=true）
#   SKIP_TESTS=false ./build.sh
#   MVN_OPTS="-T 1C" ./build.sh
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

for cmd in java mvn; do
  if ! command -v "$cmd" >/dev/null 2>&1; then
    echo "错误: 未找到命令「$cmd」。请安装 JDK 8+ 与 Maven，并确保在 PATH 中。" >&2
    exit 1
  fi
done

SKIP_TESTS="${SKIP_TESTS:-true}"
echo "==> Java"
java -version 2>&1
echo "==> Maven"
mvn -version | head -n 1

MVN_GOALS=(clean package)
if [[ "$SKIP_TESTS" == "true" ]]; then
  MVN_GOALS+=(-DskipTests)
fi

echo "==> mvn ${MVN_GOALS[*]} ${MVN_OPTS:-}"
# shellcheck disable=SC2086
mvn "${MVN_GOALS[@]}" ${MVN_OPTS:-}

# 父 POM 先于子模块执行聚合时无法复制 jar，构建成功后在此统一拷贝到根目录 target/
OUT_DIR="${SCRIPT_DIR}/target"
mkdir -p "$OUT_DIR"
for pair in "wdd-admin:ueit-admin.jar" "wdd-user-web:ueit-user-web.jar" "wdd-user-mobile:ueit-user-mobile.jar"; do
  mod="${pair%%:*}"
  jar="${pair##*:}"
  src="${SCRIPT_DIR}/${mod}/target/${jar}"
  if [[ ! -f "$src" ]]; then
    echo "错误: 未找到 $src" >&2
    exit 1
  fi
  cp -f "$src" "$OUT_DIR/"
done
cp -f "${SCRIPT_DIR}/start.sh" "$OUT_DIR/"
chmod +x "${OUT_DIR}/start.sh"

echo
echo "构建完成。"
echo "聚合目录（可执行 jar + start.sh）: ${OUT_DIR}/"
ls -la "$OUT_DIR"/*.jar 2>/dev/null || true
echo "部署: 将 ${OUT_DIR}/ 下内容拷到服务器后执行 ./start.sh"
