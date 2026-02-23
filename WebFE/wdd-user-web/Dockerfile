# ===== Stage 1: Build =====
FROM node:lts-alpine3.22 AS builder

WORKDIR /app

COPY package*.json ./

RUN npm install

COPY . .

RUN npm run build

# ===== Stage 2: Runtime =====
FROM node:lts-alpine3.22

WORKDIR /app

COPY --from=builder /app/ueit-user-web ./dist

RUN npm install -g serve

EXPOSE 5001

CMD ["serve", "-s", "dist", "-l", "5001"]