# 浮云导航项目规则文档

## 项目概述
浮云导航 - 全栈导航应用
- **前端**: Vue 3 + Quasar + TypeScript + Vite
- **后端**: Spring Boot 4.0 + Java 21 + Maven + WebFlux + JPA/Hibernate
- **数据库**: MySQL (通过 JDBC) + H2 (开发环境)
- **包管理**: pnpm (前端) + Maven (后端)


## 前端规则
### 开发命令
```bash
# 依赖管理
pnpm install        # 安装依赖
pnpx <package>      # 执行包

# 开发
pnpm dev           # 启动开发服务器
pnpm build         # 生产构建

# 代码质量
pnpm lint          # ESLint 检查
pnpm format        # Prettier 格式化
pnpm test          # 运行测试
```

### 关键规范
- **包管理器**: 严格使用 pnpm，禁用 npm 和 yarn
- **Vue 组件**: 使用 Composition API + `<script setup>`
- **代码风格**: ESLint + Prettier
- **TypeScript**: 启用严格模式，使用类型导入

## 后端规则
### 开发命令
```bash
# Maven 命令
mvn clean compile         # 编译项目
mvn clean package         # 打包应用
mvn spring-boot:run       # 运行应用
```

### 关键规范
- **编程范式**: 响应式编程 (WebFlux + JPA)
- **包命名**: `com.fuyun.*`
- **日志**: 使用 SLF4J + Logback
- **配置**: YAML 格式配置文件
- **ORM**: 使用 JPA/Hibernate 进行数据库操作

---

*最后更新: 2025-12-23*