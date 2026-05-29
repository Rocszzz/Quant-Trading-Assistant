package com.rocs.quanttradingassistant.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.rocs.quanttradingassistant.common.ResultCode;
import com.rocs.quanttradingassistant.entity.StockInfo;
import com.rocs.quanttradingassistant.entity.UserWatchlist;
import com.rocs.quanttradingassistant.dto.WatchlistAddRequest;
import com.rocs.quanttradingassistant.exception.BusinessException;
import com.rocs.quanttradingassistant.mapper.StockInfoMapper;
import com.rocs.quanttradingassistant.mapper.UserWatchlistMapper;
import com.rocs.quanttradingassistant.service.AuthService;
import com.rocs.quanttradingassistant.service.WatchlistService;
import com.rocs.quanttradingassistant.vo.WatchlistVO;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * 用户自选股服务实现类
 *
 * @author Rocs
 * @since 2026/05/28
 */
@Service
public class WatchlistServiceImpl implements WatchlistService {

    private final AuthService authService;

    private final StockInfoMapper stockInfoMapper;

    private final UserWatchlistMapper userWatchlistMapper;

    public WatchlistServiceImpl(
            AuthService authService,
            StockInfoMapper stockInfoMapper,
            UserWatchlistMapper userWatchlistMapper
    ) {
        this.authService = authService;
        this.stockInfoMapper = stockInfoMapper;
        this.userWatchlistMapper = userWatchlistMapper;
    }

    @Override
    public List<WatchlistVO> listWatchlist(String authorization) {
        Long userId = authService.getCurrentUserId(authorization);
        List<UserWatchlist> watchlist = userWatchlistMapper.selectList(Wrappers.<UserWatchlist>lambdaQuery()
                .eq(UserWatchlist::getUserId, userId)
                .orderByDesc(UserWatchlist::getCreatedAt));
        if (watchlist.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> stockIds = watchlist.stream()
                .map(UserWatchlist::getStockId)
                .distinct()
                .toList();
        Map<Long, StockInfo> stockMap = stockInfoMapper.selectBatchIds(stockIds).stream()
                .collect(Collectors.toMap(StockInfo::getId, Function.identity(), (left, right) -> left));

        return watchlist.stream()
                .map(item -> toVO(item, stockMap.get(item.getStockId())))
                .toList();
    }

    @Override
    public WatchlistVO addWatchlist(String authorization, WatchlistAddRequest request) {
        Long userId = authService.getCurrentUserId(authorization);
        StockInfo stockInfo = stockInfoMapper.selectById(request.getStockId());
        if (stockInfo == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "股票不存在");
        }

        Long existsCount = userWatchlistMapper.selectCount(Wrappers.<UserWatchlist>lambdaQuery()
                .eq(UserWatchlist::getUserId, userId)
                .eq(UserWatchlist::getStockId, request.getStockId()));
        if (existsCount > 0) {
            throw new BusinessException(ResultCode.CONFLICT, "该股票已在自选股列表中");
        }

        UserWatchlist userWatchlist = new UserWatchlist();
        userWatchlist.setUserId(userId);
        userWatchlist.setStockId(request.getStockId());
        LocalDateTime now = LocalDateTime.now();
        userWatchlist.setCreatedAt(now);
        userWatchlist.setUpdatedAt(now);
        userWatchlistMapper.insert(userWatchlist);
        return toVO(userWatchlist, stockInfo);
    }

    @Override
    public void deleteWatchlist(String authorization, Long id) {
        Long userId = authService.getCurrentUserId(authorization);
        UserWatchlist userWatchlist = userWatchlistMapper.selectById(id);
        if (userWatchlist == null || !userId.equals(userWatchlist.getUserId())) {
            throw new BusinessException(ResultCode.NOT_FOUND, "自选股记录不存在");
        }
        userWatchlistMapper.deleteById(id);
    }

    private WatchlistVO toVO(UserWatchlist userWatchlist, StockInfo stockInfo) {
        WatchlistVO watchlistVO = new WatchlistVO();
        watchlistVO.setId(userWatchlist.getId());
        watchlistVO.setStockId(userWatchlist.getStockId());
        watchlistVO.setCreatedAt(userWatchlist.getCreatedAt());
        if (stockInfo != null) {
            watchlistVO.setSymbol(stockInfo.getSymbol());
            watchlistVO.setName(stockInfo.getName());
            watchlistVO.setExchange(stockInfo.getExchange());
            watchlistVO.setIndustry(stockInfo.getIndustry());
        }
        return watchlistVO;
    }
}
