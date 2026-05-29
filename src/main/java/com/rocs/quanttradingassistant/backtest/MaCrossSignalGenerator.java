package com.rocs.quanttradingassistant.backtest;

import com.rocs.quanttradingassistant.common.StrategyType;
import com.rocs.quanttradingassistant.common.TradeSide;
import com.rocs.quanttradingassistant.entity.MarketQuote;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * 双均线策略信号生成器
 *
 * @author Rocs
 * @since 2026/05/29
 */
@Component
public class MaCrossSignalGenerator implements StrategySignalGenerator {

    public static final String PARAM_SHORT_PERIOD = "shortPeriod";

    public static final String PARAM_LONG_PERIOD = "longPeriod";

    private static final int DEFAULT_SHORT_PERIOD = 5;

    private static final int DEFAULT_LONG_PERIOD = 20;

    private static final int DECIMAL_SCALE = 4;

    @Override
    public boolean supports(String strategyType) {
        return StrategyType.MA_CROSS.name().equals(strategyType);
    }

    @Override
    public List<TradingSignal> generateSignals(List<MarketQuote> quotes, Map<String, String> params) {
        int shortPeriod = getIntegerParam(params, PARAM_SHORT_PERIOD, DEFAULT_SHORT_PERIOD);
        int longPeriod = getIntegerParam(params, PARAM_LONG_PERIOD, DEFAULT_LONG_PERIOD);
        if (shortPeriod <= 0 || longPeriod <= 0 || shortPeriod >= longPeriod) {
            throw new IllegalArgumentException("双均线参数必须满足 0 < shortPeriod < longPeriod");
        }

        List<TradingSignal> signals = new ArrayList<>();
        boolean hasInitialPositionSignal = false;
        for (int i = 1; i < quotes.size(); i++) {
            BigDecimal previousShortMa = calculateMovingAverage(quotes, i - 1, shortPeriod);
            BigDecimal previousLongMa = calculateMovingAverage(quotes, i - 1, longPeriod);
            BigDecimal currentShortMa = calculateMovingAverage(quotes, i, shortPeriod);
            BigDecimal currentLongMa = calculateMovingAverage(quotes, i, longPeriod);
            if (currentShortMa == null || currentLongMa == null) {
                continue;
            }

            // 回测区间可能从多头排列中间开始，此时没有区间内金叉，但应允许策略在首个可计算日建仓。
            if (!hasInitialPositionSignal && previousLongMa == null && currentShortMa.compareTo(currentLongMa) > 0) {
                signals.add(new TradingSignal(quotes.get(i).getTradeDate(), TradeSide.BUY, "MA_CROSS_INITIAL_BUY"));
                hasInitialPositionSignal = true;
                continue;
            }
            if (previousShortMa == null || previousLongMa == null) {
                continue;
            }

            boolean isGoldenCross = previousShortMa.compareTo(previousLongMa) <= 0
                    && currentShortMa.compareTo(currentLongMa) > 0;
            boolean isDeathCross = previousShortMa.compareTo(previousLongMa) >= 0
                    && currentShortMa.compareTo(currentLongMa) < 0;
            if (isGoldenCross) {
                signals.add(new TradingSignal(quotes.get(i).getTradeDate(), TradeSide.BUY, "MA_CROSS_BUY"));
            } else if (isDeathCross) {
                signals.add(new TradingSignal(quotes.get(i).getTradeDate(), TradeSide.SELL, "MA_CROSS_SELL"));
            }
        }
        return signals;
    }

    private int getIntegerParam(Map<String, String> params, String paramKey, int defaultValue) {
        if (params == null || !params.containsKey(paramKey)) {
            return defaultValue;
        }
        return Integer.parseInt(params.get(paramKey));
    }

    private BigDecimal calculateMovingAverage(List<MarketQuote> quotes, int index, int period) {
        if (index < period - 1) {
            return null;
        }
        BigDecimal sum = BigDecimal.ZERO;
        for (int i = index - period + 1; i <= index; i++) {
            sum = sum.add(quotes.get(i).getClosePrice());
        }
        return sum.divide(BigDecimal.valueOf(period), DECIMAL_SCALE, RoundingMode.HALF_UP);
    }
}
