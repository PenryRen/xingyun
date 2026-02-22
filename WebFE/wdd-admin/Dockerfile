# Stage 1: 构建 Vue 项目
FROM node:lts-alpine3.22 AS builder
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Stage 2: 生产镜像，只运行 serve 提供静态文件
FROM node:lts-alpine3.22
WORKDIR /app
COPY --from=builder /app/wdd-admin/dist ./dist

# 安装 serve 提供静态文件服务
RUN npm install -g serve

EXPOSE 5000
CMD ["serve", "-s", "dist", "-l", "5000"]