## 多阶段构建前后端静态站点，使用 Nginx 统一对外提供服务

# ---------- 构建管理端 ----------
FROM node:18-alpine AS build-admin

WORKDIR /app/wdd-admin

# 仅拷贝依赖声明文件，利用缓存
COPY wdd-admin/package*.json ./

RUN npm install --registry=https://registry.npmmirror.com

# 拷贝源码并构建
COPY wdd-admin ./

RUN npm run build

# ---------- 构建用户端 ----------
FROM node:18-alpine AS build-user

WORKDIR /app/wdd-user-web

COPY wdd-user-web/package*.json ./

RUN npm install --registry=https://registry.npmmirror.com

COPY wdd-user-web ./

RUN npm run build

# ---------- 生产阶段：Nginx ----------
FROM nginx:1.27-alpine AS runtime

LABEL maintainer="xingyun" \
      description="XingYun WebFE (admin + user) with Nginx"

## 拷贝自定义 Nginx 配置
COPY docker/nginx/nginx.conf /etc/nginx/nginx.conf

## 将构建产物拷贝到 Nginx 默认静态目录
# Nginx 默认工作目录为 /usr/share/nginx，html 实际路径为 /usr/share/nginx/html
COPY --from=build-admin /app/wdd-admin/ueit-admin /usr/share/nginx/html/ueit-admin
COPY --from=build-user /app/wdd-user-web/ueit-user-web /usr/share/nginx/html/ueit-user-web

EXPOSE 80 6001 6003

CMD ["nginx", "-g", "daemon off;"]


