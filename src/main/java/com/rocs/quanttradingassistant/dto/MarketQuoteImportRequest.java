package com.rocs.quanttradingassistant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 行情导入请求对象
 *
 * @author Rocs
 * @since 2026/05/28
 */
@Schema(description = "行情导入请求对象")
public class MarketQuoteImportRequest {

    @Schema(description = "股票代码", example = "000001")
    @NotBlank(message = "股票代码不能为空")
    @Size(max = 32, message = "股票代码长度不能超过32个字符")
    private String symbol;

    @Schema(description = "交易日期", example = "2026-05-28")
    @NotNull(message = "交易日期不能为空")
    private LocalDate tradeDate;

    @Schema(description = "开盘价", example = "12.35")
    @NotNull(message = "开盘价不能为空")
    @DecimalMin(value = "0.00", message = "开盘价不能小于0")
    private BigDecimal openPrice;

    @Schema(description = "最高价", example = "12.80")
    @NotNull(message = "最高价不能为空")
    @DecimalMin(value = "0.00", message = "最高价不能小于0")
    private BigDecimal highPrice;

    @Schema(description = "最低价", example = "12.20")
    @NotNull(message = "最低价不能为空")
    @DecimalMin(value = "0.00", message = "最低价不能小于0")
    private BigDecimal lowPrice;

    @Schema(description = "收盘价", example = "12.66")
    @NotNull(message = "收盘价不能为空")
    @DecimalMin(value = "0.00", message = "收盘价不能小于0")
    private BigDecimal closePrice;

    @Schema(description = "前收盘价", example = "12.30")
    @NotNull(message = "前收盘价不能为空")
    @DecimalMin(value = "0.00", message = "前收盘价不能小于0")
    private BigDecimal preClosePrice;

    @Schema(description = "成交量", example = "1200000")
    @NotNull(message = "成交量不能为空")
    @PositiveOrZero(message = "成交量不能小于0")
    private Long volume;

    @Schema(description = "成交额", example = "15192000.00")
    @NotNull(message = "成交额不能为空")
    @DecimalMin(value = "0.00", message = "成交额不能小于0")
    private BigDecimal amount;

    @Schema(description = "涨跌幅百分比，未传入时后端按收盘价和前收盘价计算", example = "2.93")
    private BigDecimal changeRate;

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
}
