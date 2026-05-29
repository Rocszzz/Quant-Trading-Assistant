package com.rocs.quanttradingassistant.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.rocs.quanttradingassistant.backtest.BacktestEngine;
import com.rocs.quanttradingassistant.backtest.BacktestResult;
import com.rocs.quanttradingassistant.common.BacktestStatus;
import com.rocs.quanttradingassistant.common.ResultCode;
import com.rocs.quanttradingassistant.common.StrategyType;
import com.rocs.quanttradingassistant.dto.BacktestRunRequest;
import com.rocs.quanttradingassistant.entity.BacktestRecord;
import com.rocs.quanttradingassistant.entity.BacktestTrade;
import com.rocs.quanttradingassistant.entity.MarketQuote;
import com.rocs.quanttradingassistant.entity.Strategy;
import com.rocs.quanttradingassistant.entity.StrategyParam;
import com.rocs.quanttradingassistant.exception.BusinessException;
import com.rocs.quanttradingassistant.mapper.BacktestRecordMapper;
import com.rocs.quanttradingassistant.mapper.BacktestTradeMapper;
import com.rocs.quanttradingassistant.mapper.StrategyMapper;
import com.rocs.quanttradingassistant.mapper.StrategyParamMapper;
import com.rocs.quanttradingassistant.provider.MarketDataProvider;
import com.rocs.quanttradingassistant.service.AuthService;
import com.rocs.quanttradingassistant.service.BacktestService;
import com.rocs.quanttradingassistant.vo.BacktestRecordVO;
import com.rocs.quanttradingassistant.vo.BacktestTradeVO;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 回测服务实现类
 *
 * @author Rocs
 * @since 2026/05/29
 */
@Service
public class BacktestServiceImpl implements BacktestService {

    private final AuthService authService;

    private final BacktestEngine backtestEngine;

    private final MarketDataProvider marketDataProvider;

    private final StrategyMapper strategyMapper;

    private final StrategyParamMapper strategyParamMapper;

    private final BacktestRecordMapper backtestRecordMapper;

    private final BacktestTradeMapper backtestTradeMapper;

    public BacktestServiceImpl(
            AuthService authService,
            BacktestEngine backtestEngine,
            MarketDataProvider marketDataProvider,
            StrategyMapper strategyMapper,
            StrategyParamMapper strategyParamMapper,
            BacktestRecordMapper backtestRecordMapper,
            BacktestTradeMapper backtestTradeMapper
    ) {
        this.authService = authService;
        this.backtestEngine = backtestEngine;
        this.marketDataProvider = marketDataProvider;
        this.strategyMapper = strategyMapper;
        this.strategyParamMapper = strategyParamMapper;
        this.backtestRecordMapper = backtestRecordMapper;
        this.backtestTradeMapper = backtestTradeMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BacktestRecordVO runBacktest(String authorization, BacktestRunRequest request) {
        Long userId = authService.getCurrentUserId(authorization);
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "回测开始日期不能晚于结束日期");
        }

        Strategy strategy = resolveBacktestStrategy(userId, request.getStrategyId());
        if (!StrategyType.MA_CROSS.name().equals(strategy.getType())) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "当前版本只支持 MA_CROSS 策略回测");
        }

        String symbol = normalizeSymbol(request.getSymbol());
        List<MarketQuote> quotes = marketDataProvider.listHistoryQuotes(symbol, request.getStartDate(), request.getEndDate());
        if (quotes.isEmpty()) {
            throw new BusinessException(ResultCode.NOT_FOUND, "回测区间内没有历史 K 线数据");
        }

        Map<String, String> params = listStrategyParams(strategy.getId());
        BacktestResult result;
        try {
            result = backtestEngine.run(strategy.getType(), symbol, request.getInitialCash(), quotes, params);
        } catch (IllegalArgumentException ex) {
            throw new BusinessException(ResultCode.BAD_REQUEST, ex.getMessage());
        }

        BacktestRecord record = createRecord(userId, request, strategy, symbol, result);
        backtestRecordMapper.insert(record);
        for (BacktestTrade trade : result.getTrades()) {
            trade.setBacktestId(record.getId());
            backtestTradeMapper.insert(trade);
        }
        return toRecordVO(record);
    }

    @Override
    public List<BacktestRecordVO> listBacktests(String authorization) {
        Long userId = authService.getCurrentUserId(authorization);
        List<BacktestRecord> records = backtestRecordMapper.selectList(Wrappers.<BacktestRecord>lambdaQuery()
                .eq(BacktestRecord::getUserId, userId)
                .orderByDesc(BacktestRecord::getCreatedAt));
        if (records.isEmpty()) {
            return Collections.emptyList();
        }
        return records.stream()
                .map(this::toRecordVO)
                .toList();
    }

    @Override
    public BacktestRecordVO getBacktest(String authorization, Long id) {
        Long userId = authService.getCurrentUserId(authorization);
        return toRecordVO(getOwnedBacktest(userId, id));
    }

    @Override
    public List<BacktestTradeVO> listTrades(String authorization, Long id) {
        Long userId = authService.getCurrentUserId(authorization);
        getOwnedBacktest(userId, id);
        List<BacktestTrade> trades = backtestTradeMapper.selectList(Wrappers.<BacktestTrade>lambdaQuery()
                .eq(BacktestTrade::getBacktestId, id)
                .orderByAsc(BacktestTrade::getTradeDate)
                .orderByAsc(BacktestTrade::getId));
        if (trades.isEmpty()) {
            return Collections.emptyList();
        }
        return trades.stream()
                .map(this::toTradeVO)
                .toList();
    }

    private Strategy resolveBacktestStrategy(Long userId, Long strategyId) {
        if (strategyId != null) {
            Strategy strategy = getOwnedStrategy(userId, strategyId);
            if (!Boolean.TRUE.equals(strategy.getEnabled())) {
                throw new BusinessException(ResultCode.BAD_REQUEST, "策略未启用，不能执行回测");
            }
            return strategy;
        }

        List<Strategy> enabledStrategies = strategyMapper.selectList(Wrappers.<Strategy>lambdaQuery()
                .eq(Strategy::getUserId, userId)
                .eq(Strategy::getEnabled, true)
                .orderByDesc(Strategy::getUpdatedAt)
                .orderByDesc(Strategy::getCreatedAt));
        if (enabledStrategies.isEmpty()) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "当前用户没有启用的策略，请先在策略管理中启用策略");
        }
        if (enabledStrategies.size() > 1) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "当前用户启用了多个策略，请指定 strategyId 执行回测");
        }
        return enabledStrategies.get(0);
    }

    private Strategy getOwnedStrategy(Long userId, Long strategyId) {
        Strategy strategy = strategyMapper.selectById(strategyId);
        if (strategy == null || !userId.equals(strategy.getUserId())) {
            throw new BusinessException(ResultCode.NOT_FOUND, "策略不存在");
        }
        return strategy;
    }

    private BacktestRecord getOwnedBacktest(Long userId, Long id) {
        BacktestRecord record = backtestRecordMapper.selectById(id);
        if (record == null || !userId.equals(record.getUserId())) {
            throw new BusinessException(ResultCode.NOT_FOUND, "回测记录不存在");
        }
        return record;
    }

    private Map<String, String> listStrategyParams(Long strategyId) {
        return strategyParamMapper.selectList(Wrappers.<StrategyParam>lambdaQuery()
                        .eq(StrategyParam::getStrategyId, strategyId))
                .stream()
                .collect(Collectors.toMap(StrategyParam::getParamKey, StrategyParam::getParamValue, (left, right) -> left));
    }

    private BacktestRecord createRecord(
            Long userId,
            BacktestRunRequest request,
            Strategy strategy,
            String symbol,
            BacktestResult result
    ) {
        BacktestRecord record = new BacktestRecord();
        record.setUserId(userId);
        record.setStrategyId(strategy.getId());
        record.setSymbol(symbol);
        record.setStartDate(request.getStartDate());
        record.setEndDate(request.getEndDate());
        record.setInitialCash(request.getInitialCash());
        record.setFinalAsset(result.getFinalAsset());
        record.setTotalReturn(result.getTotalReturn());
        record.setMaxDrawdown(result.getMaxDrawdown());
        record.setWinRate(result.getWinRate());
        record.setTradeCount(result.getTradeCount());
        record.setStatus(BacktestStatus.SUCCESS.name());
        record.setCreatedAt(LocalDateTime.now());
        return record;
    }

    private String normalizeSymbol(String symbol) {
        return symbol.trim();
    }

    private BacktestRecordVO toRecordVO(BacktestRecord record) {
        BacktestRecordVO recordVO = new BacktestRecordVO();
        recordVO.setId(record.getId());
        recordVO.setStrategyId(record.getStrategyId());
        recordVO.setSymbol(record.getSymbol());
        recordVO.setStartDate(record.getStartDate());
        recordVO.setEndDate(record.getEndDate());
        recordVO.setInitialCash(record.getInitialCash());
        recordVO.setFinalAsset(record.getFinalAsset());
        recordVO.setTotalReturn(record.getTotalReturn());
        recordVO.setMaxDrawdown(record.getMaxDrawdown());
        recordVO.setWinRate(record.getWinRate());
        recordVO.setTradeCount(record.getTradeCount());
        recordVO.setStatus(record.getStatus());
        recordVO.setCreatedAt(record.getCreatedAt());
        return recordVO;
    }

    private BacktestTradeVO toTradeVO(BacktestTrade trade) {
        BacktestTradeVO tradeVO = new BacktestTradeVO();
        tradeVO.setId(trade.getId());
        tradeVO.setBacktestId(trade.getBacktestId());
        tradeVO.setSymbol(trade.getSymbol());
        tradeVO.setTradeDate(trade.getTradeDate());
        tradeVO.setSide(trade.getSide());
        tradeVO.setPrice(trade.getPrice());
        tradeVO.setQuantity(trade.getQuantity());
        tradeVO.setAmount(trade.getAmount());
        tradeVO.setReason(trade.getReason());
        return tradeVO;
    }
}
