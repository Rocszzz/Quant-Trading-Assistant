package com.rocs.quanttradingassistant.vo;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 行情导入结果展示对象
 *
 * @author Rocs
 * @since 2026/05/28
 */
@Schema(description = "行情导入结果展示对象")
public class MarketQuoteImportResultVO {

    @Schema(description = "导入记录数", example = "10")
    private Integer importedCount;

    public Integer getImportedCount() {
        return importedCount;
    }

    public void setImportedCount(Integer importedCount) {
        this.importedCount = importedCount;
    }
}
