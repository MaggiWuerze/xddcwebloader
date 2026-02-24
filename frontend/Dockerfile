# frontend/Dockerfile

# 1) Build stage
FROM node:24-alpine AS build
LABEL org.opencontainers.image.source="https:/https://git.maggiwuerze.de/maggiwuerze/xdcwebloader"
WORKDIR /app

COPY package*.json ./
RUN npm ci

COPY . .

RUN npm run build

# 2) Runtime stage
FROM nginx:1.27-alpine
COPY --from=build /app/build /usr/share/nginx/html

COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80