package com.rocs.quanttradingassistant.provider;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.rocs.quanttradingassistant.entity.MarketQuote;
import com.rocs.quanttradingassistant.mapper.MarketQuoteMapper;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 本地模拟行情数据源
 *
 * @author Rocs
 * @since 2026/05/28
 */
@Component
public class MockMarketDataProvider implements MarketDataProvider {

    private final MarketQuoteMapper marketQuoteMapper;

    public MockMarketDataProvider(MarketQuoteMapper marketQuoteMapper) {
        this.marketQuoteMapper = marketQuoteMapper;
    }

    @Override
    public MarketQuote getLatestQuote(String symbol) {
        return marketQuoteMapper.selectOne(Wrappers.<MarketQuote>lambdaQuery()
                .eq(MarketQuote::getSymbol, symbol)
                .orderByDesc(MarketQuote::getTradeDate)
                .last("limit 1"));
    }

    @Override
    public List<MarketQuote> listHistoryQuotes(String symbol, LocalDate startDate, LocalDate endDate) {
        return marketQuoteMapper.selectList(Wrappers.<MarketQuote>lambdaQuery()
                .eq(MarketQuote::getSymbol, symbol)
                .ge(startDate != null, MarketQuote::getTradeDate, startDate)
                .le(endDate != null, MarketQuote::getTradeDate, endDate)
                .orderByAsc(MarketQuote::getTradeDate));
    }
}
