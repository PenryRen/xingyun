# ===== Stage 1: Build =====
FROM node:lts-alpine3.22 AS builder

WORKDIR /app

COPY package*.json ./

RUN npm install

COPY . .

# .env.production 会被自动读取（--mode production）
ARG VITE_APP_LOCAL_DEVELOPMENT=false
ENV VITE_APP_LOCAL_DEVELOPMENT=${VITE_APP_LOCAL_DEVELOPMENT}

RUN npm run build

# ===== Stage 2: Runtime (nginx) =====
FROM nginx:alpine

# 复制构建产物
COPY --from=builder /app/ueit-admin /usr/share/nginx/html

# 复制 nginx 站点配置
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
