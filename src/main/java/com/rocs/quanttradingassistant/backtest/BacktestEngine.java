package com.rocs.quanttradingassistant.backtest;

import com.rocs.quanttradingassistant.common.TradeSide;
import com.rocs.quanttradingassistant.entity.BacktestTrade;
import com.rocs.quanttradingassistant.entity.MarketQuote;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * 回测引擎
 *
 * @author Rocs
 * @since 2026/05/29
 */
@Component
public class BacktestEngine {

    public static final String PARAM_POSITION_RATIO = "positionRatio";

    public static final String PARAM_FEE_RATE = "feeRate";

    private static final BigDecimal DEFAULT_POSITION_RATIO = BigDecimal.ONE;

    private static final BigDecimal DEFAULT_FEE_RATE = BigDecimal.ZERO;

    private static final int AMOUNT_SCALE = 4;

    private static final int RATIO_SCALE = 6;

    private final List<StrategySignalGenerator> signalGenerators;

    public BacktestEngine(List<StrategySignalGenerator> signalGenerators) {
        this.signalGenerators = signalGenerators;
    }

    /**
     * 执行策略回测
     *
     * @param strategyType 策略类型
     * @param symbol 股票代码
     * @param initialCash 初始资金
     * @param quotes 历史 K 线列表
     * @param params 策略参数
     * @return 回测结果
     */
    public BacktestResult run(
            String strategyType,
            String symbol,
            BigDecimal initialCash,
            List<MarketQuote> quotes,
            Map<String, String> params
    ) {
        if (quotes == null || quotes.isEmpty()) {
            throw new IllegalArgumentException("历史 K 线数据不能为空");
        }

        StrategySignalGenerator signalGenerator = resolveSignalGenerator(strategyType);
        Map<LocalDate, TradingSignal> signalMap = signalGenerator.generateSignals(quotes, params).stream()
                .collect(Collectors.toMap(TradingSignal::getTradeDate, Function.identity(), (left, right) -> left));
        BigDecimal positionRatio = getDecimalParam(params, PARAM_POSITION_RATIO, DEFAULT_POSITION_RATIO);
        BigDecimal feeRate = getDecimalParam(params, PARAM_FEE_RATE, DEFAULT_FEE_RATE);
        checkRatio(positionRatio, PARAM_POSITION_RATIO);
        checkRatio(feeRate, PARAM_FEE_RATE);

        BigDecimal cash = initialCash;
        BigDecimal quantity = BigDecimal.ZERO;
        BigDecimal peakAsset = initialCash;
        BigDecimal maxDrawdown = BigDecimal.ZERO;
        BigDecimal openAmount = BigDecimal.ZERO;
        int closedTradeCount = 0;
        int winCount = 0;
        List<BacktestTrade> trades = new ArrayList<>();

        for (MarketQuote quote : quotes) {
            BigDecimal closePrice = quote.getClosePrice();
            TradingSignal signal = signalMap.get(quote.getTradeDate());

            // 回测只根据策略信号调整模拟账户，不包含真实下单、券商接口或外部交易调用。
            if (signal != null && TradeSide.BUY == signal.getSide() && quantity.compareTo(BigDecimal.ZERO) == 0) {
                BigDecimal availableCash = cash.multiply(positionRatio)
                        .divide(BigDecimal.ONE.add(feeRate), AMOUNT_SCALE, RoundingMode.DOWN);
                BigDecimal buyQuantity = availableCash.divide(closePrice, AMOUNT_SCALE, RoundingMode.DOWN);
                BigDecimal buyAmount = buyQuantity.multiply(closePrice).setScale(AMOUNT_SCALE, RoundingMode.HALF_UP);
                BigDecimal fee = buyAmount.multiply(feeRate).setScale(AMOUNT_SCALE, RoundingMode.HALF_UP);
                if (buyQuantity.compareTo(BigDecimal.ZERO) > 0 && cash.compareTo(buyAmount.add(fee)) >= 0) {
                    cash = cash.subtract(buyAmount).subtract(fee);
                    quantity = quantity.add(buyQuantity);
                    openAmount = buyAmount;
                    trades.add(createTrade(symbol, quote, TradeSide.BUY, buyQuantity, buyAmount, signal.getReason()));
                }
            } else if (signal != null && TradeSide.SELL == signal.getSide() && quantity.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal sellAmount = quantity.multiply(closePrice).setScale(AMOUNT_SCALE, RoundingMode.HALF_UP);
                BigDecimal fee = sellAmount.multiply(feeRate).setScale(AMOUNT_SCALE, RoundingMode.HALF_UP);
                cash = cash.add(sellAmount).subtract(fee);
                if (sellAmount.compareTo(openAmount) > 0) {
                    winCount++;
                }
                closedTradeCount++;
                trades.add(createTrade(symbol, quote, TradeSide.SELL, quantity, sellAmount, signal.getReason()));
                quantity = BigDecimal.ZERO;
                openAmount = BigDecimal.ZERO;
            }

            BigDecimal currentAsset = cash.add(quantity.multiply(closePrice)).setScale(AMOUNT_SCALE, RoundingMode.HALF_UP);
            if (currentAsset.compareTo(peakAsset) > 0) {
                peakAsset = currentAsset;
            }
            if (peakAsset.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal drawdown = peakAsset.subtract(currentAsset)
                        .divide(peakAsset, RATIO_SCALE, RoundingMode.HALF_UP);
                if (drawdown.compareTo(maxDrawdown) > 0) {
                    maxDrawdown = drawdown;
                }
            }
        }

        BigDecimal finalAsset = cash.add(quantity.multiply(quotes.get(quotes.size() - 1).getClosePrice()))
                .setScale(AMOUNT_SCALE, RoundingMode.HALF_UP);
        BacktestResult result = new BacktestResult();
        result.setFinalAsset(finalAsset);
        result.setTotalReturn(finalAsset.subtract(initialCash).divide(initialCash, RATIO_SCALE, RoundingMode.HALF_UP));
        result.setMaxDrawdown(maxDrawdown);
        result.setWinRate(calculateWinRate(winCount, closedTradeCount));
        result.setTradeCount(trades.size());
        result.setTrades(Collections.unmodifiableList(trades));
        return result;
    }

    private StrategySignalGenerator resolveSignalGenerator(String strategyType) {
        return signalGenerators.stream()
                .filter(generator -> generator.supports(strategyType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("暂不支持该策略类型回测"));
    }

    private BacktestTrade createTrade(
            String symbol,
            MarketQuote quote,
            TradeSide side,
            BigDecimal quantity,
            BigDecimal amount,
            String reason
    ) {
        BacktestTrade trade = new BacktestTrade();
        trade.setSymbol(symbol);
        trade.setTradeDate(quote.getTradeDate());
        trade.setSide(side.name());
        trade.setPrice(quote.getClosePrice());
        trade.setQuantity(quantity);
        trade.setAmount(amount);
        trade.setReason(reason);
        return trade;
    }

    private BigDecimal calculateWinRate(int winCount, int closedTradeCount) {
        if (closedTradeCount == 0) {
            return BigDecimal.ZERO.setScale(RATIO_SCALE, RoundingMode.HALF_UP);
        }
        return BigDecimal.valueOf(winCount).divide(BigDecimal.valueOf(closedTradeCount), RATIO_SCALE, RoundingMode.HALF_UP);
    }

    private BigDecimal getDecimalParam(Map<String, String> params, String paramKey, BigDecimal defaultValue) {
        if (params == null || !params.containsKey(paramKey)) {
            return defaultValue;
        }
        return new BigDecimal(params.get(paramKey));
    }

    private void checkRatio(BigDecimal ratio, String paramKey) {
        if (ratio.compareTo(BigDecimal.ZERO) < 0 || ratio.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException(paramKey + " 必须在 0 到 1 之间");
        }
    }
}
