# syntax=docker/dockerfile:1

# ---- build stage: produce the static SPA bundle ----
FROM node:18-alpine AS build
WORKDIR /app

# Install deps first (cached unless package*.json changes).
COPY package*.json ./
RUN npm ci

# Then build the app.
COPY . .
RUN npm run build

# ---- runtime stage: serve static files via nginx ----
FROM nginx:stable-alpine AS runtime
COPY --from=build /app/dist /usr/share/nginx/html
# SPA history-mode fallback (so deep links / refresh don't 404).
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
