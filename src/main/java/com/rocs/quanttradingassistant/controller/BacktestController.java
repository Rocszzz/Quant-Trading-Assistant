package com.rocs.quanttradingassistant.controller;

import com.rocs.quanttradingassistant.common.Result;
import com.rocs.quanttradingassistant.dto.BacktestRunRequest;
import com.rocs.quanttradingassistant.service.BacktestService;
import com.rocs.quanttradingassistant.vo.BacktestRecordVO;
import com.rocs.quanttradingassistant.vo.BacktestTradeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 策略回测控制器
 *
 * @author Rocs
 * @since 2026/05/29
 */
@Validated
@Tag(name = "策略回测模块")
@RestController
@RequestMapping("/api/backtests")
public class BacktestController {

    private final BacktestService backtestService;

    public BacktestController(BacktestService backtestService) {
        this.backtestService = backtestService;
    }

    /**
     * 运行策略回测
     *
     * @param authorization 认证请求头
     * @param request 运行回测请求参数
     * @return 回测记录
     */
    @Operation(summary = "运行策略回测")
    @PostMapping("/run")
    public Result<BacktestRecordVO> runBacktest(
            @Parameter(description = "Bearer token")
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @Valid @RequestBody BacktestRunRequest request
    ) {
        return Result.success(backtestService.runBacktest(authorization, request));
    }

    /**
     * 查询当前用户回测记录列表
     *
     * @param authorization 认证请求头
     * @return 回测记录列表
     */
    @Operation(summary = "查询我的回测记录列表")
    @GetMapping
    public Result<List<BacktestRecordVO>> listBacktests(
            @Parameter(description = "Bearer token")
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        return Result.success(backtestService.listBacktests(authorization));
    }

    /**
     * 查询回测记录详情
     *
     * @param authorization 认证请求头
     * @param id 回测ID
     * @return 回测记录详情
     */
    @Operation(summary = "查询回测记录详情")
    @GetMapping("/{id}")
    public Result<BacktestRecordVO> getBacktest(
            @Parameter(description = "Bearer token")
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @Parameter(description = "回测ID")
            @Min(value = 1, message = "回测ID必须大于0")
            @PathVariable Long id
    ) {
        return Result.success(backtestService.getBacktest(authorization, id));
    }

    /**
     * 查询回测成交记录
     *
     * @param authorization 认证请求头
     * @param id 回测ID
     * @return 回测成交记录列表
     */
    @Operation(summary = "查询回测成交记录")
    @GetMapping("/{id}/trades")
    public Result<List<BacktestTradeVO>> listTrades(
            @Parameter(description = "Bearer token")
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @Parameter(description = "回测ID")
            @Min(value = 1, message = "回测ID必须大于0")
            @PathVariable Long id
    ) {
        return Result.success(backtestService.listTrades(authorization, id));
    }
}
