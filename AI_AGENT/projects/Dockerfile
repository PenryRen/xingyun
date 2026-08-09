FROM python:3.11-slim

# 安装系统依赖
RUN apt-get update && apt-get install -y \
    gcc \
    libglib2.0-dev \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# 复制依赖文件
COPY requirements_core.txt .

# 安装 Python 依赖
RUN pip install --no-cache-dir -r requirements_core.txt

# 复制项目文件
COPY . .

# 暴露端口
EXPOSE 8000

# 设置环境变量（这些应该通过 docker-compose 或运行时提供）
ENV PYTHONUNBUFFERED=1
ENV COZE_WORKSPACE_PATH=/app

# 启动服务
WORKDIR /app/src
CMD ["python", "-m", "uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]
