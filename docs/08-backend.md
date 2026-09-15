# 后端设计

## 1. 分层架构

系统采用经典三层架构：

```
Controller（控制器层）
    ↓
Service（业务逻辑层）
    ↓
Repository（数据访问层）
    ↓
Database（数据库）
```

### 1.1 Controller 层职责
- 接收 HTTP 请求
- 参数校验
- 调用 Service
- 返回统一响应格式

### 1.2 Service 层职责
- 业务逻辑处理
- 事务管理
- 数据转换（Entity ↔ DTO）

### 1.3 Repository 层职责
- 数据库访问
- 查询构建
- 分页处理

## 2. 目录结构

```
src/main/java/com/ningshang/
├── NingshangApplication.java          # 启动类
├── config/
│   ├── WebConfig.java                 # Web 配置（跨域、静态资源）
│   ├── DataInitializer.java           # 数据初始化
│   └── CorsConfig.java                # 跨域配置
├── controller/
│   ├── HomeController.java            # 首页 + API
│   ├── AboutController.java           # 集团概况
│   ├── NewsController.java            # 新闻中心
│   ├── IndustryController.java        # 集团产业
│   └── ContactController.java         # 联系宁商
├── service/
│   ├── NewsService.java
│   ├── SubsidiaryService.java
│   ├── TeamService.java
│   ├── HonorService.java
│   ├── MilestoneService.java
│   ├── JobService.java
│   └── MessageService.java
├── repository/
│   ├── NewsRepository.java
│   ├── SubsidiaryRepository.java
│   ├── TeamMemberRepository.java
│   ├── HonorRepository.java
│   ├── MilestoneRepository.java
│   ├── JobRepository.java
│   └── MessageRepository.java
├── entity/
│   ├── News.java
│   ├── Subsidiary.java
│   ├── TeamMember.java
│   ├── Honor.java
│   ├── Milestone.java
│   ├── Job.java
│   └── Message.java
├── dto/
│   ├── ApiResponse.java               # 统一响应
│   └── PageResult.java                 # 分页结果
└── exception/
    ├── GlobalExceptionHandler.java    # 全局异常处理
    └── BusinessException.java         # 业务异常
```

## 3. 统一响应封装

```java
@Data
public class ApiResponse<T> {
    private Integer code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> resp = new ApiResponse<>();
        resp.setCode(200);
        resp.setMessage("success");
        resp.setData(data);
        return resp;
    }

    public static <T> ApiResponse<T> error(Integer code, String message) {
        ApiResponse<T> resp = new ApiResponse<>();
        resp.setCode(code);
        resp.setMessage(message);
        return resp;
    }
}
```

## 4. 全局异常处理

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ApiResponse<?> handleBusinessException(BusinessException e) {
        return ApiResponse.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleException(Exception e) {
        return ApiResponse.error(500, "服务器内部错误");
    }
}
```

## 5. 数据初始化

使用 Spring Boot CommandLineRunner 实现数据初始化：

```java
@Component
public class DataInitializer implements CommandLineRunner {

    @Override
    public void run(String[] args) {
        if (newsRepository.count() == 0) {
            initNews();
        }
        // ... 初始化其他数据
    }
}
```

## 6. 配置说明

### 6.1 application.yml

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| server.port | 服务端口 | 8080 |
| spring.datasource.url | 数据库连接 | jdbc:mysql://localhost:3306/ningshang |
| spring.datasource.username | 数据库用户名 | root |
| spring.datasource.password | 数据库密码 | root |
| spring.jpa.hibernate.ddl-auto | 自动建表 | update |
| spring.jpa.show-sql | 显示 SQL | false |

### 6.2 CORS 配置

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("*")
                .allowCredentials(true);
    }
}
```

## 7. 日志规范

- 使用 SLF4J + Logback
- 日志级别：INFO（生产）/ DEBUG（开发）
- 关键操作记录日志
- 异常日志包含堆栈信息
