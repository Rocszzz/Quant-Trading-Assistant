# 已启用策略列表接口说明

## 查询已启用策略列表

`GET /api/strategies/enabled`

说明：返回当前登录用户 `enabled = true` 的策略列表，供回测模块选择启用策略。

请求头：

```http
Authorization: Bearer <token>
```

响应数据结构与 `GET /api/strategies` 一致。
