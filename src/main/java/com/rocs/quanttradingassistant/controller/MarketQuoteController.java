package com.rocs.quanttradingassistant.controller;

import com.rocs.quanttradingassistant.common.Result;
import com.rocs.quanttradingassistant.dto.MarketQuoteHistoryRequest;
import com.rocs.quanttradingassistant.dto.MarketQuoteImportRequest;
import com.rocs.quanttradingassistant.service.MarketQuoteService;
import com.rocs.quanttradingassistant.vo.MarketKlineVO;
import com.rocs.quanttradingassistant.vo.MarketQuoteImportResultVO;
import com.rocs.quanttradingassistant.vo.MarketQuoteVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 行情数据控制器
 *
 * @author Rocs
 * @since 2026/05/28
 */
@Tag(name = "行情数据模块")
@RestController
@RequestMapping("/api/market/quotes")
public class MarketQuoteController {

    private final MarketQuoteService marketQuoteService;

    public MarketQuoteController(MarketQuoteService marketQuoteService) {
        this.marketQuoteService = marketQuoteService;
    }

    /**
     * 查询股票最新行情
     *
     * @param symbol 股票代码
     * @return 最新行情
     */
    @Operation(summary = "查询股票最新行情")
    @GetMapping("/{symbol}/latest")
    public Result<MarketQuoteVO> getLatestQuote(
            @Parameter(description = "股票代码")
            @PathVariable String symbol
    ) {
        return Result.success(marketQuoteService.getLatestQuote(symbol));
    }

    /**
     * 查询股票历史K线
     *
     * @param symbol 股票代码
     * @param request 历史行情查询请求参数
     * @return ECharts K线数据
     */
    @Operation(summary = "查询股票历史K线")
    @GetMapping("/{symbol}/history")
    public Result<MarketKlineVO> listHistoryQuotes(
            @Parameter(description = "股票代码")
            @PathVariable String symbol,
            @Valid @ModelAttribute MarketQuoteHistoryRequest request
    ) {
        return Result.success(marketQuoteService.listHistoryQuotes(symbol, request));
    }

    /**
     * 查询当前用户自选股行情
     *
     * @param authorization 认证请求头
     * @return 自选股行情列表
     */
    @Operation(summary = "查询当前用户自选股行情")
    @GetMapping("/watchlist")
    public Result<List<MarketQuoteVO>> listWatchlistQuotes(
            @Parameter(description = "Bearer token")
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        return Result.success(marketQuoteService.listWatchlistQuotes(authorization));
    }

    /**
     * 导入JSON数组行情数据
     *
     * @param requests 行情导入请求数组
     * @return 导入结果
     */
    @Operation(summary = "导入JSON数组行情数据")
    @PostMapping("/import")
    public Result<MarketQuoteImportResultVO> importQuotes(
            @Valid @RequestBody List<@Valid MarketQuoteImportRequest> requests
    ) {
        return Result.success(marketQuoteService.importQuotes(requests));
    }
}
