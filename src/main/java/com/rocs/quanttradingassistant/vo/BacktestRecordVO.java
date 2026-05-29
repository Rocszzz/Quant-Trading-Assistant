package com.rocs.quanttradingassistant.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 回测记录展示对象
 *
 * @author Rocs
 * @since 2026/05/29
 */
@Schema(description = "回测记录展示对象")
public class BacktestRecordVO {

    @Schema(description = "回测ID", example = "1")
    private Long id;

    @Schema(description = "策略ID", example = "1")
    private Long strategyId;

    @Schema(description = "股票代码", example = "000001")
    private String symbol;

    @Schema(description = "回测开始日期", example = "2026-05-04")
    private LocalDate startDate;

    @Schema(description = "回测结束日期", example = "2026-05-29")
    private LocalDate endDate;

    @Schema(description = "初始资金", example = "100000.0000")
    private BigDecimal initialCash;

    @Schema(description = "最终资产", example = "105000.0000")
    private BigDecimal finalAsset;

    @Schema(description = "总收益率", example = "0.0500")
    private BigDecimal totalReturn;

    @Schema(description = "最大回撤", example = "0.0200")
    private BigDecimal maxDrawdown;

    @Schema(description = "胜率", example = "0.5000")
    private BigDecimal winRate;

    @Schema(description = "交易次数", example = "2")
    private Integer tradeCount;

    @Schema(description = "回测状态", example = "SUCCESS")
    private String status;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Long strategyId) {
        this.strategyId = strategyId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getInitialCash() {
        return initialCash;
    }

    public void setInitialCash(BigDecimal initialCash) {
        this.initialCash = initialCash;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
