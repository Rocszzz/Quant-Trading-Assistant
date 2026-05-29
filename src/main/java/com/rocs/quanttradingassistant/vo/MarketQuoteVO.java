package com.rocs.quanttradingassistant.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 行情展示对象
 *
 * @author Rocs
 * @since 2026/05/28
 */
@Schema(description = "行情展示对象")
public class MarketQuoteVO {

    @Schema(description = "股票代码", example = "000001")
    private String symbol;

    @Schema(description = "交易日期", example = "2026-05-28")
    private LocalDate tradeDate;

    @Schema(description = "开盘价", example = "12.35")
    private BigDecimal openPrice;

    @Schema(description = "最高价", example = "12.80")
    private BigDecimal highPrice;

    @Schema(description = "最低价", example = "12.20")
    private BigDecimal lowPrice;

    @Schema(description = "收盘价", example = "12.66")
    private BigDecimal closePrice;

    @Schema(description = "前收盘价", example = "12.30")
    private BigDecimal preClosePrice;

    @Schema(description = "成交量", example = "1200000")
    private Long volume;

    @Schema(description = "成交额", example = "15192000.00")
    private BigDecimal amount;

    @Schema(description = "涨跌幅百分比", example = "2.93")
    private BigDecimal changeRate;

    @Schema(description = "5日均线", example = "12.42")
    private BigDecimal ma5;

    @Schema(description = "10日均线", example = "12.18")
    private BigDecimal ma10;

    @Schema(description = "20日均线", example = "11.96")
    private BigDecimal ma20;

    @Schema(description = "ECharts K线数组：[open, close, low, high]")
    private BigDecimal[] klineValue;

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

    public BigDecimal getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(BigDecimal openPrice) {
        this.openPrice = openPrice;
    }

    public BigDecimal getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(BigDecimal highPrice) {
        this.highPrice = highPrice;
    }

    public BigDecimal getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(BigDecimal lowPrice) {
        this.lowPrice = lowPrice;
    }

    public BigDecimal getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(BigDecimal closePrice) {
        this.closePrice = closePrice;
    }

    public BigDecimal getPreClosePrice() {
        return preClosePrice;
    }

    public void setPreClosePrice(BigDecimal preClosePrice) {
        this.preClosePrice = preClosePrice;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getChangeRate() {
        return changeRate;
    }

    public void setChangeRate(BigDecimal changeRate) {
        this.changeRate = changeRate;
    }

    public BigDecimal getMa5() {
        return ma5;
    }

    public void setMa5(BigDecimal ma5) {
        this.ma5 = ma5;
    }

    public BigDecimal getMa10() {
        return ma10;
    }

    public void setMa10(BigDecimal ma10) {
        this.ma10 = ma10;
    }

    public BigDecimal getMa20() {
        return ma20;
    }

    public void setMa20(BigDecimal ma20) {
        this.ma20 = ma20;
    }

    public BigDecimal[] getKlineValue() {
        return klineValue;
    }

    public void setKlineValue(BigDecimal[] klineValue) {
        this.klineValue = klineValue;
    }
}
