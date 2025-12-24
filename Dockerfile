# 多阶段构建 - 前端构建阶段
FROM node:22-alpine AS frontend-builder

# 设置工作目录
WORKDIR /app/frontend

# 安装pnpm
RUN npm install -g pnpm

# 复制前端package文件（先复制依赖文件以利用Docker层缓存）
COPY fuyun-front/package*.json fuyun-front/pnpm-lock.yaml ./

# 安装前端依赖（这层变化不频繁，应该放在前面）
RUN pnpm install --frozen-lockfile

# 复制前端源代码（这层变化频繁，应该放在后面）
COPY fuyun-front/ ./

# 构建前端生产版本
RUN pnpm build

# 后端构建阶段
FROM maven:3.9.6-eclipse-temurin-21-alpine AS backend-builder

# 设置工作目录
WORKDIR /app

# 复制后端pom.xml（先复制依赖文件以利用Docker层缓存）
COPY fuyun-backend/pom.xml ./

# 下载Maven依赖（利用Docker层缓存，这层变化不频繁）
RUN mvn dependency:go-offline -B

# 复制后端源代码（这层变化频繁，应该放在后面）
COPY fuyun-backend/src ./src

# 创建static目录并复制前端构建物
RUN mkdir -p src/main/resources/static && \
    cp -r /app/frontend/dist/quasar-app-base/* src/main/resources/static/

# 构建后端应用
RUN mvn clean package -DskipTests

# 运行阶段 - 最小化运行时镜像
FROM eclipse-temurin:21-jre-alpine

# 安装必要的系统包
RUN apk add --no-cache \
    tzdata \
    wget \
    && rm -rf /var/cache/apk/*

# 设置时区
ENV TZ=Asia/Shanghai

# 创建应用用户（遵循最小权限原则）
RUN addgroup -g 1001 -S appuser && \
    adduser -S appuser -u 1001 -G appuser

# 设置工作目录
WORKDIR /app

# 复制JAR文件（只复制必要的文件）
COPY --from=backend-builder /app/target/*.jar app.jar

# 更改文件所有者
RUN chown -R appuser:appuser /app

# 切换到应用用户
USER appuser

# 暴露端口
EXPOSE 8080

# 健康检查（确保服务正常运行）
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/ || exit 1

# 启动应用
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]