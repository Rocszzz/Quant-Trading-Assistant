package com.rocs.quanttradingassistant.controller;

import com.rocs.quanttradingassistant.common.Result;
import com.rocs.quanttradingassistant.dto.StrategyCreateRequest;
import com.rocs.quanttradingassistant.dto.StrategyParamUpdateRequest;
import com.rocs.quanttradingassistant.dto.StrategyUpdateRequest;
import com.rocs.quanttradingassistant.service.StrategyService;
import com.rocs.quanttradingassistant.vo.StrategyVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 策略管理控制器
 *
 * @author Rocs
 * @since 2026/05/29
 */
@Validated
@Tag(name = "策略管理模块")
@RestController
@RequestMapping("/api/strategies")
public class StrategyController {

    private final StrategyService strategyService;

    public StrategyController(StrategyService strategyService) {
        this.strategyService = strategyService;
    }

    /**
     * 查询当前用户策略列表
     *
     * @param authorization 认证请求头
     * @return 策略列表
     */
    @Operation(summary = "查询我的策略列表")
    @GetMapping
    public Result<List<StrategyVO>> listStrategies(
            @Parameter(description = "Bearer token")
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        return Result.success(strategyService.listStrategies(authorization));
    }

    /**
     * 创建策略配置
     *
     * @param authorization 认证请求头
     * @param request 创建策略请求参数
     * @return 策略配置
     */
    @Operation(summary = "创建策略配置")
    @PostMapping
    public Result<StrategyVO> createStrategy(
            @Parameter(description = "Bearer token")
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @Valid @RequestBody StrategyCreateRequest request
    ) {
        return Result.success(strategyService.createStrategy(authorization, request));
    }

    /**
     * 更新策略配置
     *
     * @param authorization 认证请求头
     * @param id 策略ID
     * @param request 更新策略请求参数
     * @return 策略配置
     */
    @Operation(summary = "更新策略配置")
    @PutMapping("/{id}")
    public Result<StrategyVO> updateStrategy(
            @Parameter(description = "Bearer token")
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @Parameter(description = "策略ID")
            @Min(value = 1, message = "策略ID必须大于0")
            @PathVariable Long id,
            @Valid @RequestBody StrategyUpdateRequest request
    ) {
        return Result.success(strategyService.updateStrategy(authorization, id, request));
    }

    /**
     * 删除策略配置
     *
     * @param authorization 认证请求头
     * @param id 策略ID
     * @return 统一响应
     */
    @Operation(summary = "删除策略配置")
    @DeleteMapping("/{id}")
    public Result<Void> deleteStrategy(
            @Parameter(description = "Bearer token")
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @Parameter(description = "策略ID")
            @Min(value = 1, message = "策略ID必须大于0")
            @PathVariable Long id
    ) {
        strategyService.deleteStrategy(authorization, id);
        return Result.success();
    }

    /**
     * 查询策略详情
     *
     * @param authorization 认证请求头
     * @param id 策略ID
     * @return 策略配置详情
     */
    @Operation(summary = "查询策略详情")
    @GetMapping("/{id}")
    public Result<StrategyVO> getStrategy(
            @Parameter(description = "Bearer token")
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @Parameter(description = "策略ID")
            @Min(value = 1, message = "策略ID必须大于0")
            @PathVariable Long id
    ) {
        return Result.success(strategyService.getStrategy(authorization, id));
    }

    /**
     * 覆盖更新策略参数
     *
     * @param authorization 认证请求头
     * @param id 策略ID
     * @param request 策略参数请求参数
     * @return 策略配置详情
     */
    @Operation(summary = "覆盖更新策略参数")
    @PutMapping("/{id}/params")
    public Result<StrategyVO> updateParams(
            @Parameter(description = "Bearer token")
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @Parameter(description = "策略ID")
            @Min(value = 1, message = "策略ID必须大于0")
            @PathVariable Long id,
            @Valid @RequestBody StrategyParamUpdateRequest request
    ) {
        return Result.success(strategyService.updateParams(authorization, id, request));
    }
}
