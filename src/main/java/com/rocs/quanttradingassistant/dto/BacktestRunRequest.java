package com.rocs.quanttradingassistant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 运行回测请求参数
 *
 * @author Rocs
 * @since 2026/05/29
 */
@Schema(description = "运行回测请求参数")
public class BacktestRunRequest {

    @Schema(description = "策略ID", example = "1")
    @NotNull(message = "策略ID不能为空")
    @Min(value = 1, message = "策略ID必须大于0")
    private Long strategyId;

    @Schema(description = "股票代码", example = "000001")
    @NotBlank(message = "股票代码不能为空")
    private String symbol;

    @Schema(description = "回测开始日期", example = "2026-05-04")
    @NotNull(message = "回测开始日期不能为空")
    private LocalDate startDate;

    @Schema(description = "回测结束日期", example = "2026-05-29")
    @NotNull(message = "回测结束日期不能为空")
    private LocalDate endDate;

    @Schema(description = "初始资金", example = "100000.00")
    @NotNull(message = "初始资金不能为空")
    @DecimalMin(value = "0.01", message = "初始资金必须大于0")
    private BigDecimal initialCash;

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
}
