# ===== AI Agent 后端 Dockerfile =====
# 基于 Python 3.11 运行 LangChain + FastAPI 服务

FROM python:3.11-slim

WORKDIR /app

# 安装系统依赖（gcc 用于编译 psycopg 等原生扩展）
RUN apt-get update && apt-get install -y --no-install-recommends \
    gcc \
    libpq-dev \
    && rm -rf /var/lib/apt/lists/*

# 先复制依赖文件，利用 Docker 层缓存
COPY requirements.txt ./

# 安装 Python 依赖
RUN pip install --no-cache-dir -r requirements.txt

# 复制项目代码
COPY . .

# 设置环境变量默认值（可被 docker run -e 覆盖）
ENV ENV=production
ENV PYTHONUNBUFFERED=1
ENV WORKSPACE_PATH=/app
ENV XINGYUN_MYSQL_AUTO_CREATE_AI_SCHEMA=true

# 暴露端口（与前端 nginx.conf 中 /ai-agent/ 代理目标一致）
EXPOSE 5000

# 健康检查
HEALTHCHECK --interval=30s --timeout=10s --start-period=15s --retries=3 \
    CMD python -c "import urllib.request; urllib.request.urlopen('http://localhost:5000/health', timeout=5)" || exit 1

# 使用 main.py 启动（包含 /learning/workspace、/learning/assistant、/health 等前端所需接口）
CMD ["python", "src/main.py", "-p", "5000"]
