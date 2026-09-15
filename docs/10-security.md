# 安全设计

## 1. 安全原则

- **最小权限原则**：仅开放必要接口
- **防御性编程**：所有输入需校验
- **深度防御**：多层安全防护

## 2. 接口安全

### 2.1 CORS 跨域安全
- 仅允许指定域名访问
- 限制 HTTP 方法
- 允许携带 Cookie（如需要）

### 2.2 输入校验
- 前端：表单校验（Element Plus 表单验证）
- 后端：@Valid 注解 + 自定义校验器
- SQL 注入防护：使用 JPA 参数化查询

### 2.3 XSS 防护
- 前端：v-html 内容需后端过滤
- 后端：响应头 Content-Type 正确设置
- Cookie 设置 HttpOnly

### 2.4 CSRF 防护
- Phase 1：使用 SameSite Cookie
- Phase 2：引入 CSRF Token（管理后台）

## 3. 数据安全

### 3.1 敏感数据
- 留言中的手机号、邮箱需脱敏展示
- 密码（Phase 2）使用 BCrypt 加密

### 3.2 SQL 注入防护
- 使用 JPA 参数化查询
- 禁止拼接 SQL 语句
- 使用 @Param 注解传参

## 4. 访问控制

### 4.1 接口限流（Phase 2）
- 使用 Redis + Lua 实现令牌桶
- 限制：100 次/分钟/IP

### 4.2 文件上传安全（Phase 2）
- 限制文件类型（仅图片）
- 限制文件大小（最大 5MB）
- 文件重命名（UUID）
- 存储路径隔离

## 5. 安全头设置

```
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 1; mode=block
Content-Security-Policy: default-src 'self'
```

## 6. 安全审计（Phase 2）

- 记录所有管理操作日志
- 异常登录检测
- 敏感操作二次确认
