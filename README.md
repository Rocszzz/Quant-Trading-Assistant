# Quant Trading Assistant

量化交易助手后端项目，基于 Java 19、Spring Boot 3、Maven、MyBatis-Plus、MySQL、Redis 构建。

当前阶段提供用户、股票基础信息和自选股模块，不包含真实证券交易下单能力，也不会模拟点击任何证券 App。

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
├── common          # 统一响应、通用枚举、工具类
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

## 统一响应结构

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

## 阶段 1 接口说明

### 用户模块

#### POST /api/auth/register

用户注册，注册成功后直接返回登录 token。

```json
{
  "username": "student001",
  "password": "student123",
  "nickname": "量化学习者"
}
```

#### POST /api/auth/login

用户登录。

```json
{
  "username": "admin",
  "password": "admin123"
}
```

#### GET /api/auth/me

查询当前登录用户信息。

```text
Authorization: Bearer 登录令牌
```

#### PUT /api/auth/nickname

修改当前用户昵称。

```text
Authorization: Bearer 登录令牌
```

```json
{
  "nickname": "量化研究员"
}
```

#### POST /api/auth/logout

退出当前登录会话。

```text
Authorization: Bearer 登录令牌
```

### 股票基础信息模块

#### GET /api/stocks

查询数据库中的股票基础信息列表。

#### GET /api/stocks/search

按股票代码或名称模糊搜索。

```text
GET /api/stocks/search?keyword=平安
```

### 自选股模块

#### GET /api/watchlist

查询当前用户自选股列表。

```text
Authorization: Bearer 登录令牌
```

#### POST /api/watchlist

添加自选股，重复添加会返回业务错误。

```text
Authorization: Bearer 登录令牌
```

```json
{
  "stockId": 1
}
```

#### DELETE /api/watchlist/{id}

删除当前用户自己的自选股记录。

```text
Authorization: Bearer 登录令牌
```

## 演示账号

初始化脚本会创建演示账号：

```text
username: admin
password: admin123
```
