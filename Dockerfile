# Unified Dockerfile for XingYun (Frontend + Backend)

# ---------- Stage 1: Build Backend ----------
FROM maven:3.9.6-eclipse-temurin-8 AS build-be
WORKDIR /app/be
COPY WebBE/pom.xml .
COPY WebBE/wdd-admin/pom.xml wdd-admin/
COPY WebBE/wdd-user-web/pom.xml wdd-user-web/
COPY WebBE/wdd-user-mobile/pom.xml wdd-user-mobile/

# COPY sources
COPY WebBE/wdd-admin/src wdd-admin/src
COPY WebBE/wdd-user-web/src wdd-user-web/src
COPY WebBE/wdd-user-mobile/src wdd-user-mobile/src

RUN mvn clean package -DskipTests

# ---------- Stage 2: Build Frontend (Admin) ----------
FROM node:18-alpine AS build-fe-admin
WORKDIR /app/fe-admin
COPY WebFE/wdd-admin/package*.json ./
RUN npm install --registry=https://registry.npmmirror.com
COPY WebFE/wdd-admin/ ./
RUN npm run build

# ---------- Stage 3: Build Frontend (User) ----------
FROM node:18-alpine AS build-fe-user
WORKDIR /app/fe-user
COPY WebFE/wdd-user-web/package*.json ./
RUN npm install --registry=https://registry.npmmirror.com
COPY WebFE/wdd-user-web/ ./
RUN npm run build

# ---------- Stage 4: Runtime Stage ----------
FROM eclipse-temurin:8-jre-alpine AS runtime

# Install Nginx
RUN apk add --no-cache nginx

WORKDIR /app

# Copy Backend Jars
COPY --from=build-be /app/be/wdd-admin/target/ueit-admin.jar /app/ueit-admin.jar
COPY --from=build-be /app/be/wdd-user-web/target/ueit-user-web.jar /app/ueit-user-web.jar
COPY --from=build-be /app/be/wdd-user-mobile/target/ueit-user-mobile.jar /app/ueit-user-mobile.jar

# Copy Frontend Static Files
# Note: Adjusting paths to match Alpine Nginx default prefix (/var/lib/nginx) and nginx.conf relative paths
COPY --from=build-fe-admin /app/fe-admin/ueit-admin /var/lib/nginx/html/ueit-admin
COPY --from=build-fe-user /app/fe-user/ueit-user-web /var/lib/nginx/html/ueit-user-web

# Create directory for ueupload alias defined in nginx.conf
RUN mkdir -p /var/lib/nginx/html/resource/web-file

# Copy Nginx Configuration
COPY nginx.conf /etc/nginx/nginx.conf

# Setup Timezone
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Copy Entrypoint
COPY entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh

# Expose ports
EXPOSE 80 6001 6003 6007

ENTRYPOINT ["/app/entrypoint.sh"]
