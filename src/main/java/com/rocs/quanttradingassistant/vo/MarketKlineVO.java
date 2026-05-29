package com.rocs.quanttradingassistant.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;

/**
 * ECharts K线展示对象
 *
 * @author Rocs
 * @since 2026/05/28
 */
@Schema(description = "ECharts K线展示对象")
public class MarketKlineVO {

    @Schema(description = "股票代码", example = "000001")
    private String symbol;

    @Schema(description = "交易日期数组")
    private List<String> dates;

    @Schema(description = "K线数据数组，每项格式为：[open, close, low, high]")
    private List<BigDecimal[]> values;

    @Schema(description = "成交量数组")
    private List<Long> volumes;

    @Schema(description = "涨跌幅数组")
    private List<BigDecimal> changeRates;

    @Schema(description = "5日均线数组")
    private List<BigDecimal> ma5;

    @Schema(description = "10日均线数组")
    private List<BigDecimal> ma10;

    @Schema(description = "20日均线数组")
    private List<BigDecimal> ma20;

    @Schema(description = "明细行情")
    private List<MarketQuoteVO> quotes;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    public List<BigDecimal[]> getValues() {
        return values;
    }

    public void setValues(List<BigDecimal[]> values) {
        this.values = values;
    }

    public List<Long> getVolumes() {
        return volumes;
    }

    public void setVolumes(List<Long> volumes) {
        this.volumes = volumes;
    }

    public List<BigDecimal> getChangeRates() {
        return changeRates;
    }

    public void setChangeRates(List<BigDecimal> changeRates) {
        this.changeRates = changeRates;
    }

    public List<BigDecimal> getMa5() {
        return ma5;
    }

    public void setMa5(List<BigDecimal> ma5) {
        this.ma5 = ma5;
    }

    public List<BigDecimal> getMa10() {
        return ma10;
    }

    public void setMa10(List<BigDecimal> ma10) {
        this.ma10 = ma10;
    }

    public List<BigDecimal> getMa20() {
        return ma20;
    }

    public void setMa20(List<BigDecimal> ma20) {
        this.ma20 = ma20;
    }

    public List<MarketQuoteVO> getQuotes() {
        return quotes;
    }

    public void setQuotes(List<MarketQuoteVO> quotes) {
        this.quotes = quotes;
    }
}
