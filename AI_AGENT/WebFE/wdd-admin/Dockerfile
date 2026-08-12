# ===== Stage 1: Build =====
FROM node:lts-alpine3.22 AS builder

WORKDIR /app

COPY package*.json ./

RUN npm install

COPY . .

ARG VITE_APP_LOCAL_DEVELOPMENT=false
ENV VITE_APP_LOCAL_DEVELOPMENT=${VITE_APP_LOCAL_DEVELOPMENT}

RUN npm run build

# ===== Stage 2: Runtime =====
FROM node:lts-alpine3.22

WORKDIR /app

COPY --from=builder /app/ueit-admin ./dist

RUN npm install -g serve

EXPOSE 5002

CMD ["serve", "-s", "dist", "-l", "5002"]
