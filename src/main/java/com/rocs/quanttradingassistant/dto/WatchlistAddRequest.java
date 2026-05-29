package com.rocs.quanttradingassistant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * 添加自选股请求参数
 *
 * @author Rocs
 * @since 2026/05/28
 */
@Schema(description = "添加自选股请求参数")
public class WatchlistAddRequest {

    @Schema(description = "股票ID", example = "1")
    @NotNull(message = "股票ID不能为空")
    private Long stockId;

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }
}
