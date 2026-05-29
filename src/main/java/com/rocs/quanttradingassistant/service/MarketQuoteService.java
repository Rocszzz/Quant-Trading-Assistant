package com.rocs.quanttradingassistant.service;

import com.rocs.quanttradingassistant.dto.MarketQuoteHistoryRequest;
import com.rocs.quanttradingassistant.dto.MarketQuoteImportRequest;
import com.rocs.quanttradingassistant.vo.MarketKlineVO;
import com.rocs.quanttradingassistant.vo.MarketQuoteImportResultVO;
import com.rocs.quanttradingassistant.vo.MarketQuoteVO;
import java.util.List;

/**
 * 行情服务接口
 *
 * @author Rocs
 * @since 2026/05/28
 */
public interface MarketQuoteService {

    /**
     * 查询股票最新行情
     *
     * @param symbol 股票代码
     * @return 最新行情
     */
    MarketQuoteVO getLatestQuote(String symbol);

    /**
     * 查询股票历史K线
     *
     * @param symbol 股票代码
     * @param request 历史行情查询请求参数
     * @return ECharts K线数据
     */
    MarketKlineVO listHistoryQuotes(String symbol, MarketQuoteHistoryRequest request);

    /**
     * 查询当前用户自选股最新行情
     *
     * @param authorization HTTP Authorization 请求头
     * @return 自选股行情列表
     */
    List<MarketQuoteVO> listWatchlistQuotes(String authorization);

    /**
     * 导入行情数据
     *
     * @param requests 行情导入请求数组
     * @return 导入结果
     */
    MarketQuoteImportResultVO importQuotes(List<MarketQuoteImportRequest> requests);
}
