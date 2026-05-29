# 行情数据模块接口文档

## 说明

- 本模块仅提供行情数据查询、历史 K 线展示数据和 JSON 数组导入能力。
- 不接入真实证券交易，不包含自动下单逻辑。
- 价格、成交额、涨跌幅均使用 `BigDecimal` 对应的 JSON number 返回。

## 1. 查询最新行情

**GET** `/api/market/quotes/{symbol}/latest`

### 请求参数

| 参数 | 位置 | 必填 | 说明 |
| --- | --- | --- | --- |
| symbol | path | 是 | 股票代码，例如 `000001` |

### 响应示例

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "symbol": "000001",
    "tradeDate": "2026-05-28",
    "openPrice": 12.35,
    "highPrice": 12.80,
    "lowPrice": 12.20,
    "closePrice": 12.66,
    "preClosePrice": 12.30,
    "volume": 1200000,
    "amount": 15192000.00,
    "changeRate": 2.9268,
    "ma5": 12.4200,
    "ma10": 12.1800,
    "ma20": 11.9600,
    "klineValue": [12.35, 12.66, 12.20, 12.80]
  }
}
```

## 2. 查询历史 K 线

**GET** `/api/market/quotes/{symbol}/history?startDate=2026-05-01&endDate=2026-05-28`

### 请求参数

| 参数 | 位置 | 必填 | 说明 |
| --- | --- | --- | --- |
| symbol | path | 是 | 股票代码，例如 `000001` |
| startDate | query | 否 | 开始交易日期，格式 `yyyy-MM-dd` |
| endDate | query | 否 | 结束交易日期，格式 `yyyy-MM-dd` |

### 响应示例

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "symbol": "000001",
    "dates": ["2026-05-27", "2026-05-28"],
    "values": [
      [12.10, 12.30, 12.00, 12.45],
      [12.35, 12.66, 12.20, 12.80]
    ],
    "volumes": [980000, 1200000],
    "changeRates": [1.2345, 2.9268],
    "ma5": [null, null],
    "ma10": [null, null],
    "ma20": [null, null],
    "quotes": [
      {
        "symbol": "000001",
        "tradeDate": "2026-05-27",
        "openPrice": 12.10,
        "highPrice": 12.45,
        "lowPrice": 12.00,
        "closePrice": 12.30,
        "preClosePrice": 12.15,
        "volume": 980000,
        "amount": 12054000.00,
        "changeRate": 1.2345,
        "ma5": null,
        "ma10": null,
        "ma20": null,
        "klineValue": [12.10, 12.30, 12.00, 12.45]
      }
    ]
  }
}
```

## 3. 查询当前用户自选股行情

**GET** `/api/market/quotes/watchlist`

### 请求头

| 参数 | 必填 | 说明 |
| --- | --- | --- |
| Authorization | 是 | `Bearer token` |

### 响应示例

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "symbol": "000001",
      "tradeDate": "2026-05-28",
      "openPrice": 12.35,
      "highPrice": 12.80,
      "lowPrice": 12.20,
      "closePrice": 12.66,
      "preClosePrice": 12.30,
      "volume": 1200000,
      "amount": 15192000.00,
      "changeRate": 2.9268,
      "ma5": 12.4200,
      "ma10": 12.1800,
      "ma20": 11.9600,
      "klineValue": [12.35, 12.66, 12.20, 12.80]
    }
  ]
}
```

## 4. 导入行情数据

**POST** `/api/market/quotes/import`

### 请求体示例

```json
[
  {
    "symbol": "000001",
    "tradeDate": "2026-05-27",
    "openPrice": 12.10,
    "highPrice": 12.45,
    "lowPrice": 12.00,
    "closePrice": 12.30,
    "preClosePrice": 12.15,
    "volume": 980000,
    "amount": 12054000.00
  },
  {
    "symbol": "000001",
    "tradeDate": "2026-05-28",
    "openPrice": 12.35,
    "highPrice": 12.80,
    "lowPrice": 12.20,
    "closePrice": 12.66,
    "preClosePrice": 12.30,
    "volume": 1200000,
    "amount": 15192000.00,
    "changeRate": 2.9268
  }
]
```

### 响应示例

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "importedCount": 2
  }
}
```

## 5. ECharts 使用提示

- `dates` 可作为 K 线图 `xAxis.data`。
- `values` 可作为 candlestick 序列数据，单项格式为 `[open, close, low, high]`。
- `ma5`、`ma10`、`ma20` 可作为折线序列数据。
- `volumes` 可作为成交量柱状图数据。
