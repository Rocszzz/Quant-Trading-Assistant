# 策略管理模块接口说明

## 说明

- 所有接口路径前缀为 `/api/strategies`。
- 所有接口都需要在请求头中传入 `Authorization: Bearer <token>`。
- 策略只做配置管理，不执行真实交易。
- 当前支持策略类型：`MA_CROSS` 双均线策略、`MACD` MACD 策略、`BREAKOUT` 突破策略。
- 策略参数以 key-value 形式保存，参数类型支持 `NUMBER`、`STRING`、`BOOLEAN`。

## 查询策略列表

`GET /api/strategies`

响应数据：

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "默认双均线策略模板",
      "code": "ma_cross_default",
      "type": "MA_CROSS",
      "typeDescription": "双均线策略",
      "description": "短期均线上穿长期均线时形成买入观察信号，仅用于策略配置和后续回测，不执行真实交易。",
      "enabled": true,
      "createdAt": "2026-05-29T10:00:00",
      "updatedAt": "2026-05-29T10:00:00",
      "params": []
    }
  ]
}
```

## 创建策略

`POST /api/strategies`

请求体：

```json
{
  "name": "双均线策略",
  "code": "ma_cross_default",
  "type": "MA_CROSS",
  "description": "短期均线上穿长期均线时形成买入观察信号",
  "enabled": true,
  "params": [
    {
      "paramKey": "shortPeriod",
      "paramValue": "5",
      "paramType": "NUMBER",
      "remark": "短期均线周期"
    },
    {
      "paramKey": "longPeriod",
      "paramValue": "20",
      "paramType": "NUMBER",
      "remark": "长期均线周期"
    }
  ]
}
```

## 更新策略

`PUT /api/strategies/{id}`

请求体：

```json
{
  "name": "双均线策略",
  "code": "ma_cross_default",
  "type": "MA_CROSS",
  "description": "仅用于策略配置和后续回测",
  "enabled": true
}
```

## 删除策略

`DELETE /api/strategies/{id}`

说明：删除策略时会同步删除该策略参数。

## 查询策略详情

`GET /api/strategies/{id}`

说明：只能查询当前登录用户自己的策略。

## 覆盖更新策略参数

`PUT /api/strategies/{id}/params`

请求体：

```json
{
  "params": [
    {
      "paramKey": "shortPeriod",
      "paramValue": "10",
      "paramType": "NUMBER",
      "remark": "短期均线周期"
    },
    {
      "paramKey": "longPeriod",
      "paramValue": "30",
      "paramType": "NUMBER",
      "remark": "长期均线周期"
    },
    {
      "paramKey": "signalMode",
      "paramValue": "CROSS_UP",
      "paramType": "STRING",
      "remark": "信号模式"
    }
  ]
}
```

说明：该接口采用整体覆盖方式保存参数，旧参数会先删除再写入新参数。
