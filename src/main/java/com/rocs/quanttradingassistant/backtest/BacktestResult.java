package com.rocs.quanttradingassistant.backtest;

import com.rocs.quanttradingassistant.entity.BacktestTrade;
import java.math.BigDecimal;
import java.util.List;

/**
 * 回测计算结果
 *
 * @author Rocs
 * @since 2026/05/29
 */
public class BacktestResult {

    private BigDecimal finalAsset;

    private BigDecimal totalReturn;

    private BigDecimal maxDrawdown;

    private BigDecimal winRate;

    private Integer tradeCount;

    private List<BacktestTrade> trades;

    public BigDecimal getFinalAsset() {
        return finalAsset;
    }

    public void setFinalAsset(BigDecimal finalAsset) {
        this.finalAsset = finalAsset;
    }

    public BigDecimal getTotalReturn() {
        return totalReturn;
    }

    public void setTotalReturn(BigDecimal totalReturn) {
        this.totalReturn = totalReturn;
    }

    public BigDecimal getMaxDrawdown() {
        return maxDrawdown;
    }

    public void setMaxDrawdown(BigDecimal maxDrawdown) {
        this.maxDrawdown = maxDrawdown;
    }

    public BigDecimal getWinRate() {
        return winRate;
    }

    public void setWinRate(BigDecimal winRate) {
        this.winRate = winRate;
    }

    public Integer getTradeCount() {
        return tradeCount;
    }

    public void setTradeCount(Integer tradeCount) {
        this.tradeCount = tradeCount;
    }

    public List<BacktestTrade> getTrades() {
        return trades;
    }

    public void setTrades(List<BacktestTrade> trades) {
        this.trades = trades;
    }
}
