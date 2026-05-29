package com.rocs.quanttradingassistant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 历史行情查询请求参数
 *
 * @author Rocs
 * @since 2026/05/28
 */
@Schema(description = "历史行情查询请求参数")
public class MarketQuoteHistoryRequest {

    @Schema(description = "开始交易日期", example = "2026-05-01")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Schema(description = "结束交易日期", example = "2026-05-28")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

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
}
