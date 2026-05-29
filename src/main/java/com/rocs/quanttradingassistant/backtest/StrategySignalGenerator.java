package com.rocs.quanttradingassistant.backtest;

import com.rocs.quanttradingassistant.entity.MarketQuote;
import java.util.List;
import java.util.Map;

/**
 * 策略信号生成接口
 *
 * @author Rocs
 * @since 2026/05/29
 */
public interface StrategySignalGenerator {

    /**
     * 判断当前信号生成器是否支持指定策略类型
     *
     * @param strategyType 策略类型
     * @return 是否支持
     */
    boolean supports(String strategyType);

    /**
     * 根据历史 K 线生成交易信号
     *
     * @param quotes 历史 K 线列表
     * @param params 策略参数
     * @return 交易信号列表
     */
    List<TradingSignal> generateSignals(List<MarketQuote> quotes, Map<String, String> params);
}
