package com.rocs.quanttradingassistant.backtest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.rocs.quanttradingassistant.common.TradeSide;
import com.rocs.quanttradingassistant.entity.MarketQuote;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class MaCrossSignalGeneratorTest {

    private final MaCrossSignalGenerator signalGenerator = new MaCrossSignalGenerator();

    @Test
    void generateSignalsShouldCreateBuyAndSellWhenMovingAverageCrosses() {
        Map<String, String> params = new HashMap<>(2);
        params.put(MaCrossSignalGenerator.PARAM_SHORT_PERIOD, "2");
        params.put(MaCrossSignalGenerator.PARAM_LONG_PERIOD, "3");

        List<TradingSignal> signals = signalGenerator.generateSignals(List.of(
                quote("2026-05-01", "10"),
                quote("2026-05-02", "9"),
                quote("2026-05-03", "8"),
                quote("2026-05-04", "12"),
                quote("2026-05-05", "14"),
                quote("2026-05-06", "10"),
                quote("2026-05-07", "6")
        ), params);

        assertEquals(2, signals.size());
        assertEquals(LocalDate.of(2026, 5, 4), signals.get(0).getTradeDate());
        assertEquals(TradeSide.BUY, signals.get(0).getSide());
        assertEquals("MA_CROSS_BUY", signals.get(0).getReason());
        assertEquals(LocalDate.of(2026, 5, 7), signals.get(1).getTradeDate());
        assertEquals(TradeSide.SELL, signals.get(1).getSide());
        assertEquals("MA_CROSS_SELL", signals.get(1).getReason());
    }

    @Test
    void generateSignalsShouldCreateInitialBuyWhenBacktestStartsInBullishTrend() {
        Map<String, String> params = new HashMap<>(2);
        params.put(MaCrossSignalGenerator.PARAM_SHORT_PERIOD, "2");
        params.put(MaCrossSignalGenerator.PARAM_LONG_PERIOD, "5");

        List<TradingSignal> signals = signalGenerator.generateSignals(List.of(
                quote("2026-05-01", "10"),
                quote("2026-05-02", "11"),
                quote("2026-05-03", "12"),
                quote("2026-05-04", "13"),
                quote("2026-05-05", "14"),
                quote("2026-05-06", "15")
        ), params);

        assertEquals(1, signals.size());
        assertEquals(LocalDate.of(2026, 5, 5), signals.get(0).getTradeDate());
        assertEquals(TradeSide.BUY, signals.get(0).getSide());
        assertEquals("MA_CROSS_INITIAL_BUY", signals.get(0).getReason());
    }

    private MarketQuote quote(String tradeDate, String closePrice) {
        MarketQuote quote = new MarketQuote();
        quote.setSymbol("000001");
        quote.setTradeDate(LocalDate.parse(tradeDate));
        quote.setClosePrice(new BigDecimal(closePrice));
        return quote;
    }
}
