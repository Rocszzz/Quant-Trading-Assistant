package com.rocs.quanttradingassistant.controller;

import com.rocs.quanttradingassistant.common.Result;
import com.rocs.quanttradingassistant.dto.WatchlistAddRequest;
import com.rocs.quanttradingassistant.service.WatchlistService;
import com.rocs.quanttradingassistant.vo.WatchlistVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户自选股控制器
 *
 * @author Rocs
 * @since 2026/05/28
 */
@Tag(name = "自选股模块")
@RestController
@RequestMapping("/api/watchlist")
public class WatchlistController {

    private final WatchlistService watchlistService;

    public WatchlistController(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }

    /**
     * 查询当前用户自选股列表
     *
     * @param authorization 认证请求头
     * @return 自选股列表
     */
    @Operation(summary = "查询我的自选股列表")
    @GetMapping
    public Result<List<WatchlistVO>> listWatchlist(
            @Parameter(description = "Bearer token")
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        return Result.success(watchlistService.listWatchlist(authorization));
    }

    /**
     * 添加自选股
     *
     * @param authorization 认证请求头
     * @param request 添加自选股请求参数
     * @return 自选股信息
     */
    @Operation(summary = "添加自选股")
    @PostMapping
    public Result<WatchlistVO> addWatchlist(
            @Parameter(description = "Bearer token")
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @Valid @RequestBody WatchlistAddRequest request
    ) {
        return Result.success(watchlistService.addWatchlist(authorization, request));
    }

    /**
     * 删除自选股
     *
     * @param authorization 认证请求头
     * @param id 自选股记录ID
     * @return 统一响应
     */
    @Operation(summary = "删除自选股")
    @DeleteMapping("/{id}")
    public Result<Void> deleteWatchlist(
            @Parameter(description = "Bearer token")
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @Parameter(description = "自选股记录ID")
            @PathVariable Long id
    ) {
        watchlistService.deleteWatchlist(authorization, id);
        return Result.success();
    }
}
