package com.rocs.quanttradingassistant.service;

import com.rocs.quanttradingassistant.dto.StockSearchRequest;
import com.rocs.quanttradingassistant.vo.StockInfoVO;
import java.util.List;

/**
 * 股票基础信息服务接口
 *
 * @author Rocs
 * @since 2026/05/28
 */
public interface StockService {

    /**
     * 查询股票基础信息列表
     *
     * @return 股票基础信息列表
     */
    List<StockInfoVO> listStocks();

    /**
     * 按股票代码或名称模糊搜索股票
     *
     * @param request 股票搜索请求参数
     * @return 股票基础信息列表
     */
    List<StockInfoVO> searchStocks(StockSearchRequest request);
}
