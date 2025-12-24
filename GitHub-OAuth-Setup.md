# GitHub OAuth 登录设置指南

## 1. 创建GitHub OAuth应用

### 步骤1：登录GitHub
1. 登录你的GitHub账户
2. 进入 Settings → Developer settings → OAuth Apps
3. 点击 "New OAuth App"

### 步骤2：填写应用信息
- **Application name**: 浮云导航 (Fuyun Navigation)
- **Homepage URL**: `http://localhost:9000`
- **Authorization callback URL**: `http://localhost:8080/api/auth/github/callback`

### 步骤3：获取凭证
创建完成后，你会获得：
- **Client ID**: 公开的标识符
- **Client Secret**: 私密的密钥

## 2. 环境变量配置

### Windows PowerShell设置环境变量：
```powershell
$env:GITHUB_CLIENT_ID = "你的GitHub Client ID"
$env:GITHUB_CLIENT_SECRET = "你的GitHub Client Secret"
$env:JWT_SECRET = "fuyun-navigation-jwt-secret-key-2024"
```

### 或创建.env文件（在fuyun-backend根目录）：
```env
GITHUB_CLIENT_ID=你的GitHub Client ID
GITHUB_CLIENT_SECRET=你的GitHub Client Secret
JWT_SECRET=fuyun-navigation-jwt-secret-key-2024
```

## 3. 重启服务

设置环境变量后需要重启后端服务：
1. 停止当前运行的后端服务（Ctrl+C）
2. 重新运行：`mvn spring-boot:run`

## 4. 测试登录流程

1. 访问 http://localhost:9000
2. 点击右上角的"GitHub 登录"按钮
3. 会跳转到GitHub授权页面
4. 授权后会自动跳转回调页面
5. 登录成功后会显示用户信息

## 5. 验证功能

### 后端API测试：
```bash
# 获取GitHub授权URL
curl http://localhost:8080/api/auth/github/url

# 获取当前用户信息（需要先登录获取token）
curl -H "Authorization: Bearer YOUR_TOKEN" http://localhost:8080/api/auth/me
```

### 前端功能验证：
- 登录按钮显示正常
- GitHub图标正确显示
- 登录后显示用户头像和菜单
- 退出登录功能正常

## 6. 数据库验证

登录成功后，用户数据会保存在H2数据库中：
1. 访问 http://localhost:8080/h2-console
2. JDBC URL: `jdbc:h2:mem:fuyunnav`
3. 用户名: `sa`，密码留空
4. 查询用户表：`SELECT * FROM users;`

## 7. 常见问题

### 问题1：回调地址错误
确保GitHub OAuth应用的回调URL与配置文件中的完全一致。

### 问题2：环境变量未生效
重启后端服务，确保环境变量被正确加载。

### 问题3：CORS错误
检查后端CORS配置是否正确包含前端域名。

### 问题4：JWT Token无效
检查JWT密钥配置和Token过期时间设置。