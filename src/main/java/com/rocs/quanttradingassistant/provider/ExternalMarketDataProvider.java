package com.rocs.quanttradingassistant.provider;

import com.rocs.quanttradingassistant.entity.MarketQuote;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * 外部行情数据源预留实现
 *
 * @author Rocs
 * @since 2026/05/28
 */
public class ExternalMarketDataProvider implements MarketDataProvider {

    @Override
    public MarketQuote getLatestQuote(String symbol) {
        return null;
    }

    @Override
    public List<MarketQuote> listHistoryQuotes(String symbol, LocalDate startDate, LocalDate endDate) {
        return Collections.emptyList();
    }
}
