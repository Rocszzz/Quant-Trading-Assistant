package com.rocs.quanttradingassistant.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * 自选股展示对象
 *
 * @author Rocs
 * @since 2026/05/28
 */
@Schema(description = "自选股展示对象")
public class WatchlistVO {

    @Schema(description = "自选股记录ID", example = "1")
    private Long id;

    @Schema(description = "股票ID", example = "1")
    private Long stockId;

    @Schema(description = "股票代码", example = "000001")
    private String symbol;

    @Schema(description = "股票名称", example = "平安银行")
    private String name;

    @Schema(description = "交易所", example = "SZSE")
    private String exchange;

    @Schema(description = "所属行业", example = "银行")
    private String industry;

    @Schema(description = "添加时间")
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
