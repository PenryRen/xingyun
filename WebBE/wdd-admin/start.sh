#!/bin/bash
# ============================================================
# 麟航智训 — 管理后端启动脚本（适配 2GB 内存服务器）
# 说明：推荐系统总内存 ≥ 2GB + 足够 swap（建议 8GB）
# ============================================================

set -euo pipefail

# ---- 环境变量：可在启动前覆盖 ----
export SPRING_PROFILES_ACTIVE="${SPRING_PROFILES_ACTIVE:-prod}"

# ---- MySQL / Redis 配置（全部默认指向本机） ----
# 如果 MySQL/Redis 不在本机，请设置这些环境变量后再启动
# export MYSQL_URL="jdbc:mysql://..."
# export MYSQL_USERNAME="root"
# export MYSQL_PASSWORD="xxx"
# export REDIS_HOST="127.0.0.1"
# export REDIS_PORT="6379"
# export REDIS_PASSWORD="xxx"
# export REDIS_DB="1"

JAR_FILE="$(dirname "$0")/ueit-admin.jar"
PID_FILE="$(dirname "$0")/ueit-admin.pid"
LOG_DIR="$(dirname "$0")/log"
STDOUT_LOG="${LOG_DIR}/admin-stdout.log"

mkdir -p "${LOG_DIR}"

# ========== 2G 内存 JVM 参数 ==========
# 总堆 256MB（够用，避免 OOM killer）
# + UseG1GC：停顿更平滑
# + -XX:+HeapDumpOnOutOfMemoryError：OOM 时生成堆转储，便于排查
# + -XX:+UseSerialGC 可以再省 10~20MB 元空间开销，但 G1 响应更好
# + 压缩指针 + 类元数据上限，节省 off-heap
JVM_OPTS="
-Xms256m
-Xmx256m
-XX:+UseG1GC
-XX:MaxMetaspaceSize=128m
-XX:+UseCompressedOops
-XX:+UseCompressedClassPointers
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=${LOG_DIR}/admin-hprof
-XX:+PrintCommandLineFlags
-XX:InitialCodeCacheSize=8m
-XX:ReservedCodeCacheSize=32m
-Dfile.encoding=UTF-8
-Djava.security.egd=file:/dev/./urandom
"

start() {
    if [[ -f "${PID_FILE}" ]] && kill -0 "$(cat "${PID_FILE}")" 2>/dev/null; then
        echo "[WARN] wdd-admin 已在运行，PID=$(cat "${PID_FILE}")"
        exit 0
    fi

    echo "[INFO] 启动 wdd-admin（profile=${SPRING_PROFILES_ACTIVE}，堆上限 256MB）..."
    nohup java ${JVM_OPTS} -jar "${JAR_FILE}" >> "${STDOUT_LOG}" 2>&1 &
    echo $! > "${PID_FILE}"
    sleep 3
    if kill -0 "$(cat "${PID_FILE}")" 2>/dev/null; then
        echo "[OK]   wdd-admin 启动成功，PID=$(cat "${PID_FILE}")"
        echo "       日志: ${STDOUT_LOG}"
        echo "       端口: 16002"
    else
        echo "[FAIL] wdd-admin 启动失败，请查看 ${STDOUT_LOG}"
        rm -f "${PID_FILE}"
        exit 1
    fi
}

stop() {
    if [[ ! -f "${PID_FILE}" ]]; then
        echo "[WARN] 未发现 PID 文件，可能 wdd-admin 未启动"
        exit 0
    fi
    PID="$(cat "${PID_FILE}")"
    if ! kill -0 "${PID}" 2>/dev/null; then
        echo "[WARN] PID ${PID} 不存在，清理 PID 文件"
        rm -f "${PID_FILE}"
        exit 0
    fi
    echo "[INFO] 停止 wdd-admin，PID=${PID}..."
    kill "${PID}"
    for i in {1..20}; do
        if ! kill -0 "${PID}" 2>/dev/null; then
            rm -f "${PID_FILE}"
            echo "[OK]   wdd-admin 已停止"
            exit 0
        fi
        sleep 1
    done
    echo "[WARN] 优雅停止超时，强制 kill -9"
    kill -9 "${PID}" 2>/dev/null
    rm -f "${PID_FILE}"
    echo "[OK]   已强制停止"
}

status() {
    if [[ -f "${PID_FILE}" ]] && kill -0 "$(cat "${PID_FILE}")" 2>/dev/null; then
        echo "[OK] wdd-admin 运行中，PID=$(cat "${PID_FILE}")，端口 16002"
    else
        echo "[X]  wdd-admin 未运行"
    fi
}

case "${1:-start}" in
    start)  start  ;;
    stop)   stop   ;;
    restart) stop; start ;;
    status) status ;;
    *) echo "用法: $0 {start|stop|restart|status}" ;;
esac
