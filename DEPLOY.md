# 浮云导航 Docker 部署说明

## 项目结构

```
fuyun-navigation/
├── Dockerfile                    # 统一的Docker构建文件
├── docker-compose.yml           # Docker Compose编排文件
├── .dockerignore               # Docker忽略文件
├── nginx.conf                  # Nginx配置（备用）
├── mysql/
│   └── init/
│       └── 01-init.sql         # MySQL初始化脚本
├── fuyun-backend/              # Spring Boot后端
│   └── src/main/resources/
│       ├── application.yml     # 开发环境配置
│       └── application-prod.yml # 生产环境配置
└── fuyun-front/               # Vue3前端
```

## 快速开始

### 1. 环境要求

- Docker 20.10+
- Docker Compose 2.0+
- 至少2GB可用内存

### 2. 克隆项目

```bash
git clone <repository-url>
cd fuyun-navigation
```

### 3. 环境变量配置（可选）

创建 `.env` 文件配置环境变量：

```bash
# GitHub OAuth配置
GITHUB_CLIENT_ID=your_github_client_id
GITHUB_CLIENT_SECRET=your_github_client_secret
GITHUB_REDIRECT_URI=http://localhost:8080/api/auth/github/callback

# JWT密钥
JWT_SECRET=your_custom_jwt_secret_key

# 数据库密码（可选，默认为fuyun123456）
MYSQL_ROOT_PASSWORD=fuyun123456
MYSQL_PASSWORD=fuyun123456
```

### 4. 构建和启动

```bash
# 构建并启动所有服务
docker-compose up -d --build

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f app
```

### 5. 访问应用

- 应用地址：http://localhost:8080
- MySQL端口：3306
- Redis端口：6379

## 服务说明

### 应用服务 (app)

- **端口**：8080
- **包含**：前端 + 后端
- **特性**：
  - 自动构建前端并集成到Spring Boot
  - 健康检查
  - 日志收集
  - 生产环境优化

### MySQL服务 (mysql)

- **版本**：MySQL 8.0
- **端口**：3306
- **数据卷**：mysql_data
- **初始化**：自动执行SQL脚本

### Redis服务 (redis)

- **版本**：Redis 7
- **端口**：6379
- **密码**：fuyun123456
- **持久化**：AOF模式

## 开发环境

### 本地开发

```bash
# 启动依赖服务（MySQL + Redis）
docker-compose up -d mysql redis

# 启动前端开发服务器
cd fuyun-front
pnpm install
pnpm dev

# 启动后端开发服务器
cd fuyun-backend
mvn spring-boot:run
```

### 生产环境

```bash
# 完整生产环境
docker-compose -f docker-compose.yml up -d --build
```

## 常用命令

```bash
# 重新构建并启动
docker-compose up -d --build

# 停止服务
docker-compose down

# 停止并删除数据卷（谨慎使用）
docker-compose down -v

# 查看日志
docker-compose logs -f [service_name]

# 进入容器
docker-compose exec app sh

# 数据库连接
docker-compose exec mysql mysql -u fuyun -p fuyun_navigation

# 备份数据库
docker-compose exec mysql mysqldump -u root -p fuyun_navigation > backup.sql

# 恢复数据库
docker-compose exec -i mysql mysql -u root -p fuyun_navigation < backup.sql
```

## 配置说明

### Spring Boot配置

- **开发环境**：application.yml（H2内存数据库）
- **生产环境**：application-prod.yml（MySQL）

### 环境变量

| 变量名 | 默认值 | 说明 |
|--------|--------|------|
| SPRING_PROFILES_ACTIVE | prod | Spring环境配置 |
| GITHUB_CLIENT_ID | - | GitHub OAuth客户端ID |
| GITHUB_CLIENT_SECRET | - | GitHub OAuth客户端密钥 |
| JWT_SECRET | fuyun-navigation-jwt-secret-key-2024 | JWT签名密钥 |
| MYSQL_ROOT_PASSWORD | fuyun123456 | MySQL root密码 |

## 故障排除

### 常见问题

1. **端口冲突**
   ```bash
   # 修改docker-compose.yml中的端口映射
   ports:
     - "8081:8080"  # 改为8081
   ```

2. **内存不足**
   ```bash
   # 增加Docker内存限制或关闭其他容器
   ```

3. **数据库连接失败**
   ```bash
   # 检查MySQL服务状态
   docker-compose logs mysql
   
   # 重启MySQL服务
   docker-compose restart mysql
   ```

4. **前端构建失败**
   ```bash
   # 清理并重新构建
   docker-compose down
   docker system prune -f
   docker-compose up -d --build
   ```

### 日志位置

- **应用日志**：`app_logs` 数据卷，容器内 `/app/logs/fuyun-backend.log`
- **MySQL日志**：Docker日志 `docker-compose logs mysql`
- **Redis日志**：Docker日志 `docker-compose logs redis`

## 更新部署

```bash
# 拉取最新代码
git pull

# 重新构建并部署
docker-compose up -d --build

# 数据库迁移（如需要）
docker-compose exec app java -jar app.jar --spring.profiles.active=prod
```

## 安全建议

1. **修改默认密码**：更改MySQL和Redis的默认密码
2. **网络安全**：生产环境建议使用内部网络
3. **HTTPS配置**：配置SSL证书启用HTTPS
4. **定期备份**：设置定时备份MySQL数据

## 许可证

MIT License