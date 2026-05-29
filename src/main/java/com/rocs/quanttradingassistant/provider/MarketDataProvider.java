package com.rocs.quanttradingassistant.provider;

import com.rocs.quanttradingassistant.entity.MarketQuote;
import java.time.LocalDate;
import java.util.List;

/**
 * 行情数据源接口
 *
 * @author Rocs
 * @since 2026/05/28
 */
public interface MarketDataProvider {

    /**
     * 查询股票最新行情
     *
     * @param symbol 股票代码
     * @return 最新行情
     */
    MarketQuote getLatestQuote(String symbol);

    /**
     * 查询股票历史行情
     *
     * @param symbol 股票代码
     * @param startDate 开始交易日期
     * @param endDate 结束交易日期
     * @return 历史行情列表
     */
    List<MarketQuote> listHistoryQuotes(String symbol, LocalDate startDate, LocalDate endDate);
}
