package com.rocs.quanttradingassistant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 回测成交记录实体类
 *
 * @author Rocs
 * @since 2026/05/29
 */
@TableName("backtest_trade")
public class BacktestTrade {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long backtestId;

    private String symbol;

    private LocalDate tradeDate;

    private String side;

    private BigDecimal price;

    private BigDecimal quantity;

    private BigDecimal amount;

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

    @Override
    public String toString() {
        return "BacktestTrade{"
                + "id=" + id
                + ", backtestId=" + backtestId
                + ", symbol='" + symbol + '\''
                + ", tradeDate=" + tradeDate
                + ", side='" + side + '\''
                + '}';
    }
}
