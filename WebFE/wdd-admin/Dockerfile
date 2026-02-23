# ===== Stage 1: Build =====
FROM node:lts-alpine3.29 AS builder

WORKDIR /app

COPY package*.json ./

RUN npm install

COPY . .

RUN npm run build

# ===== Stage 2: Runtime =====
FROM node:lts-alpine3.29

WORKDIR /app

COPY --from=builder /app/ueit-admin ./dist

RUN npm install -g serve

EXPOSE 5002

CMD ["serve", "-s", "dist", "-l", "5002"]