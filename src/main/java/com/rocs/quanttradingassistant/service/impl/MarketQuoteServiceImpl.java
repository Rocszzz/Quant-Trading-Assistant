package com.rocs.quanttradingassistant.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.rocs.quanttradingassistant.common.ResultCode;
import com.rocs.quanttradingassistant.dto.MarketQuoteHistoryRequest;
import com.rocs.quanttradingassistant.dto.MarketQuoteImportRequest;
import com.rocs.quanttradingassistant.entity.MarketQuote;
import com.rocs.quanttradingassistant.entity.StockInfo;
import com.rocs.quanttradingassistant.entity.UserWatchlist;
import com.rocs.quanttradingassistant.exception.BusinessException;
import com.rocs.quanttradingassistant.mapper.MarketQuoteMapper;
import com.rocs.quanttradingassistant.mapper.StockInfoMapper;
import com.rocs.quanttradingassistant.mapper.UserWatchlistMapper;
import com.rocs.quanttradingassistant.provider.MarketDataProvider;
import com.rocs.quanttradingassistant.service.AuthService;
import com.rocs.quanttradingassistant.service.MarketQuoteService;
import com.rocs.quanttradingassistant.vo.MarketKlineVO;
import com.rocs.quanttradingassistant.vo.MarketQuoteImportResultVO;
import com.rocs.quanttradingassistant.vo.MarketQuoteVO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 行情服务实现类
 *
 * @author Rocs
 * @since 2026/05/28
 */
@Service
public class MarketQuoteServiceImpl implements MarketQuoteService {

    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    private static final int DECIMAL_SCALE = 4;

    private final AuthService authService;

    private final MarketDataProvider marketDataProvider;

    private final MarketQuoteMapper marketQuoteMapper;

    private final StockInfoMapper stockInfoMapper;

    private final UserWatchlistMapper userWatchlistMapper;

    public MarketQuoteServiceImpl(
            AuthService authService,
            MarketDataProvider marketDataProvider,
            MarketQuoteMapper marketQuoteMapper,
            StockInfoMapper stockInfoMapper,
            UserWatchlistMapper userWatchlistMapper
    ) {
        this.authService = authService;
        this.marketDataProvider = marketDataProvider;
        this.marketQuoteMapper = marketQuoteMapper;
        this.stockInfoMapper = stockInfoMapper;
        this.userWatchlistMapper = userWatchlistMapper;
    }

    @Override
    public MarketQuoteVO getLatestQuote(String symbol) {
        String normalizedSymbol = normalizeSymbol(symbol);
        MarketQuote latestQuote = marketDataProvider.getLatestQuote(normalizedSymbol);
        if (latestQuote == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "行情数据不存在");
        }

        List<MarketQuote> historyQuotes = marketDataProvider.listHistoryQuotes(
                normalizedSymbol, null, latestQuote.getTradeDate());
        return toQuoteVO(latestQuote, historyQuotes, historyQuotes.size() - 1);
    }

    @Override
    public MarketKlineVO listHistoryQuotes(String symbol, MarketQuoteHistoryRequest request) {
        String normalizedSymbol = normalizeSymbol(symbol);
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "开始日期不能晚于结束日期");
        }

        List<MarketQuote> quotes = marketDataProvider.listHistoryQuotes(normalizedSymbol, startDate, endDate);
        List<MarketQuoteVO> quoteVOList = toQuoteVOList(quotes);

        MarketKlineVO klineVO = new MarketKlineVO();
        klineVO.setSymbol(normalizedSymbol);
        klineVO.setDates(quoteVOList.stream().map(item -> item.getTradeDate().toString()).toList());
        klineVO.setValues(quoteVOList.stream().map(MarketQuoteVO::getKlineValue).toList());
        klineVO.setVolumes(quoteVOList.stream().map(MarketQuoteVO::getVolume).toList());
        klineVO.setChangeRates(quoteVOList.stream().map(MarketQuoteVO::getChangeRate).toList());
        klineVO.setMa5(quoteVOList.stream().map(MarketQuoteVO::getMa5).toList());
        klineVO.setMa10(quoteVOList.stream().map(MarketQuoteVO::getMa10).toList());
        klineVO.setMa20(quoteVOList.stream().map(MarketQuoteVO::getMa20).toList());
        klineVO.setQuotes(quoteVOList);
        return klineVO;
    }

    @Override
    public List<MarketQuoteVO> listWatchlistQuotes(String authorization) {
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
                .map(item -> stockMap.get(item.getStockId()))
                .filter(Objects::nonNull)
                .map(StockInfo::getSymbol)
                .map(this::getWatchlistLatestQuote)
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MarketQuoteImportResultVO importQuotes(List<MarketQuoteImportRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "导入行情数据不能为空");
        }

        LocalDateTime now = LocalDateTime.now();
        for (MarketQuoteImportRequest request : requests) {
            MarketQuote marketQuote = toMarketQuote(request, now);
            MarketQuote existsQuote = marketQuoteMapper.selectOne(Wrappers.<MarketQuote>lambdaQuery()
                    .eq(MarketQuote::getSymbol, marketQuote.getSymbol())
                    .eq(MarketQuote::getTradeDate, marketQuote.getTradeDate()));
            if (existsQuote == null) {
                marketQuoteMapper.insert(marketQuote);
            } else {
                marketQuote.setId(existsQuote.getId());
                marketQuoteMapper.updateById(marketQuote);
            }
        }

        MarketQuoteImportResultVO resultVO = new MarketQuoteImportResultVO();
        resultVO.setImportedCount(requests.size());
        return resultVO;
    }

    private String normalizeSymbol(String symbol) {
        if (symbol == null || symbol.trim().isEmpty()) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "股票代码不能为空");
        }
        return symbol.trim();
    }

    private MarketQuoteVO getWatchlistLatestQuote(String symbol) {
        MarketQuote latestQuote = marketDataProvider.getLatestQuote(symbol);
        if (latestQuote == null) {
            return null;
        }
        List<MarketQuote> historyQuotes = marketDataProvider.listHistoryQuotes(symbol, null, latestQuote.getTradeDate());
        return toQuoteVO(latestQuote, historyQuotes, historyQuotes.size() - 1);
    }

    private List<MarketQuoteVO> toQuoteVOList(List<MarketQuote> quotes) {
        return quotes.stream()
                .map(quote -> toQuoteVO(quote, quotes, quotes.indexOf(quote)))
                .toList();
    }

    private MarketQuoteVO toQuoteVO(MarketQuote quote, List<MarketQuote> quotes, int index) {
        MarketQuoteVO quoteVO = new MarketQuoteVO();
        quoteVO.setSymbol(quote.getSymbol());
        quoteVO.setTradeDate(quote.getTradeDate());
        quoteVO.setOpenPrice(quote.getOpenPrice());
        quoteVO.setHighPrice(quote.getHighPrice());
        quoteVO.setLowPrice(quote.getLowPrice());
        quoteVO.setClosePrice(quote.getClosePrice());
        quoteVO.setPreClosePrice(quote.getPreClosePrice());
        quoteVO.setVolume(quote.getVolume());
        quoteVO.setAmount(quote.getAmount());
        quoteVO.setChangeRate(resolveChangeRate(quote));
        quoteVO.setMa5(calculateMovingAverage(quotes, index, 5));
        quoteVO.setMa10(calculateMovingAverage(quotes, index, 10));
        quoteVO.setMa20(calculateMovingAverage(quotes, index, 20));
        quoteVO.setKlineValue(new BigDecimal[]{
                quote.getOpenPrice(),
                quote.getClosePrice(),
                quote.getLowPrice(),
                quote.getHighPrice()
        });
        return quoteVO;
    }

    private MarketQuote toMarketQuote(MarketQuoteImportRequest request, LocalDateTime now) {
        MarketQuote marketQuote = new MarketQuote();
        marketQuote.setSymbol(normalizeSymbol(request.getSymbol()));
        marketQuote.setTradeDate(request.getTradeDate());
        marketQuote.setOpenPrice(request.getOpenPrice());
        marketQuote.setHighPrice(request.getHighPrice());
        marketQuote.setLowPrice(request.getLowPrice());
        marketQuote.setClosePrice(request.getClosePrice());
        marketQuote.setPreClosePrice(request.getPreClosePrice());
        marketQuote.setVolume(request.getVolume());
        marketQuote.setAmount(request.getAmount());
        marketQuote.setChangeRate(request.getChangeRate() == null ? calculateChangeRate(request) : request.getChangeRate());
        marketQuote.setCreatedAt(now);
        return marketQuote;
    }

    private BigDecimal resolveChangeRate(MarketQuote quote) {
        if (quote.getChangeRate() != null) {
            return quote.getChangeRate();
        }
        return calculateChangeRate(quote.getClosePrice(), quote.getPreClosePrice());
    }

    private BigDecimal calculateChangeRate(MarketQuoteImportRequest request) {
        return calculateChangeRate(request.getClosePrice(), request.getPreClosePrice());
    }

    private BigDecimal calculateChangeRate(BigDecimal closePrice, BigDecimal preClosePrice) {
        if (preClosePrice == null || BigDecimal.ZERO.compareTo(preClosePrice) == 0) {
            return BigDecimal.ZERO;
        }
        return closePrice.subtract(preClosePrice)
                .multiply(ONE_HUNDRED)
                .divide(preClosePrice, DECIMAL_SCALE, RoundingMode.HALF_UP);
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
