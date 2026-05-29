package com.rocs.quanttradingassistant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

/**
 * 股票搜索请求参数
 *
 * @author Rocs
 * @since 2026/05/28
 */
@Schema(description = "股票搜索请求参数")
public class StockSearchRequest {

    @Schema(description = "搜索关键字，支持股票代码或名称模糊搜索", example = "平安")
    @Size(max = 64, message = "搜索关键字长度不能超过64个字符")
    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
