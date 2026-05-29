# 策略回测模块接口说明

## 说明

- 所有接口路径前缀为 `/api/backtests`。
- 所有接口都需要在请求头中传入 `Authorization: Bearer <token>`。
- 当前版本只支持 `MA_CROSS` 双均线策略回测。
- 回测只使用历史 K 线做模拟账户计算，不执行真实下单，也不对接任何券商接口。
- 策略参数从 `strategy_param` 读取，当前支持：
  - `shortPeriod`：短期均线周期，默认 `5`。
  - `longPeriod`：长期均线周期，默认 `20`。
  - `positionRatio`：买入仓位比例，默认 `1`，范围 `0` 到 `1`。
  - `feeRate`：手续费率预留参数，默认 `0`，范围 `0` 到 `1`。

## 运行回测

`POST /api/backtests/run`

请求体：

```json
{
  "strategyId": 1,
  "symbol": "000001",
  "startDate": "2026-05-04",
  "endDate": "2026-05-29",
  "initialCash": 100000.00
}
```

响应数据：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "strategyId": 1,
    "symbol": "000001",
    "startDate": "2026-05-04",
    "endDate": "2026-05-29",
    "initialCash": 100000.0000,
    "finalAsset": 102345.6700,
    "totalReturn": 0.023457,
    "maxDrawdown": 0.012300,
    "winRate": 1.000000,
    "tradeCount": 2,
    "status": "SUCCESS",
    "createdAt": "2026-05-29T15:30:00"
  }
}
```

## 查询回测记录列表

`GET /api/backtests`

响应数据：

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "strategyId": 1,
      "symbol": "000001",
      "startDate": "2026-05-04",
      "endDate": "2026-05-29",
      "initialCash": 100000.0000,
      "finalAsset": 102345.6700,
      "totalReturn": 0.023457,
      "maxDrawdown": 0.012300,
      "winRate": 1.000000,
      "tradeCount": 2,
      "status": "SUCCESS",
      "createdAt": "2026-05-29T15:30:00"
    }
  ]
}
```

## 查询回测记录详情

`GET /api/backtests/{id}`

说明：只能查询当前登录用户自己的回测记录。

## 查询回测成交记录

`GET /api/backtests/{id}/trades`

响应数据：

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "backtestId": 1,
      "symbol": "000001",
      "tradeDate": "2026-05-15",
      "side": "BUY",
      "price": 12.7400,
      "quantity": 7849.2935,
      "amount": 99999.9992,
      "reason": "MA_CROSS_BUY"
    }
  ]
}
```

## 测试样例

单元测试覆盖双均线策略信号：

```powershell
mvn test -Dtest=MaCrossSignalGeneratorTest
```
