package com.rocs.quanttradingassistant.controller;

import com.rocs.quanttradingassistant.common.Result;
import com.rocs.quanttradingassistant.dto.StockSearchRequest;
import com.rocs.quanttradingassistant.service.StockService;
import com.rocs.quanttradingassistant.vo.StockInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 股票基础信息控制器
 *
 * @author Rocs
 * @since 2026/05/28
 */
@Tag(name = "股票基础信息模块")
@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    /**
     * 查询股票列表
     *
     * @return 股票列表
     */
    @Operation(summary = "查询股票列表")
    @GetMapping
    public Result<List<StockInfoVO>> listStocks() {
        return Result.success(stockService.listStocks());
    }

    /**
     * 按股票代码或名称搜索股票
     *
     * @param request 股票搜索请求参数
     * @return 股票列表
     */
    @Operation(summary = "搜索股票")
    @GetMapping("/search")
    public Result<List<StockInfoVO>> searchStocks(@Valid @ModelAttribute StockSearchRequest request) {
        return Result.success(stockService.searchStocks(request));
    }
}
