package com.rocs.quanttradingassistant.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 回测成交记录展示对象
 *
 * @author Rocs
 * @since 2026/05/29
 */
@Schema(description = "回测成交记录展示对象")
public class BacktestTradeVO {

    @Schema(description = "成交ID", example = "1")
    private Long id;

    @Schema(description = "回测ID", example = "1")
    private Long backtestId;

    @Schema(description = "股票代码", example = "000001")
    private String symbol;

    @Schema(description = "交易日期", example = "2026-05-15")
    private LocalDate tradeDate;

    @Schema(description = "交易方向", example = "BUY")
    private String side;

    @Schema(description = "成交价格", example = "12.7400")
    private BigDecimal price;

    @Schema(description = "成交数量", example = "1000.0000")
    private BigDecimal quantity;

    @Schema(description = "成交金额", example = "12740.0000")
    private BigDecimal amount;

    @Schema(description = "成交原因", example = "MA_CROSS_BUY")
    private String reason;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBacktestId() {
        return backtestId;
    }

    public void setBacktestId(Long backtestId) {
        this.backtestId = backtestId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public LocalDate getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(LocalDate tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
