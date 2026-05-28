# Quant Trading Assistant

量化交易助手后端项目，基于 Java 19、Spring Boot 3、Maven、MyBatis-Plus、MySQL、Redis 构建。

当前阶段仅提供后端基础骨架和演示登录能力，不包含真实证券交易下单能力，也不会模拟点击任何证券 App。

## 技术栈

- Java 19
- Spring Boot 3.x
- Maven
- MyBatis-Plus
- MySQL
- Redis
- Knife4j / OpenAPI 3

## 目录结构

```text
src/main/java/com/rocs/quanttradingassistant
├── common          # 统一响应、通用枚举
├── config          # 项目配置
├── controller      # HTTP 接口
├── dto             # 请求参数对象
├── entity          # 数据库实体
├── exception       # 业务异常和全局异常处理
├── mapper          # MyBatis-Plus Mapper
├── service         # 业务接口
├── service/impl    # 业务实现
└── vo              # 响应展示对象
```

## 环境准备

1. 安装 JDK 19。
2. 安装 Maven 3.8+。
3. 启动 MySQL，并执行初始化脚本：

```bash
mysql -uroot -p < docs/sql/init.sql
```

4. 启动 Redis，默认连接 `127.0.0.1:6379`。
5. 按需修改 `src/main/resources/application.yml` 中的 MySQL 和 Redis 配置。

## 启动项目

```bash
mvn spring-boot:run
```

启动后默认服务地址：

- 后端接口：http://localhost:8080
- Knife4j 文档：http://localhost:8080/doc.html
- Swagger UI：http://localhost:8080/swagger-ui.html

## 演示账号

默认演示账号配置在 `application.yml`：

```yaml
quant:
  auth:
    username: admin
    password: admin123
```

## 登录接口

### POST /api/auth/login

请求体：

```json
{
  "username": "admin",
  "password": "admin123"
}
```

响应体：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "登录令牌",
    "user": {
      "id": 1,
      "username": "admin",
      "nickname": "量化助手演示用户"
    }
  }
}
```

### GET /api/auth/me

请求头：

```text
Authorization: Bearer 登录令牌
```

### POST /api/auth/logout

请求头：

```text
Authorization: Bearer 登录令牌
```
