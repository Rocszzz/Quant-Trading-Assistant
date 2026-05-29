package com.rocs.quanttradingassistant.service;

import com.rocs.quanttradingassistant.dto.BacktestRunRequest;
import com.rocs.quanttradingassistant.vo.BacktestRecordVO;
import com.rocs.quanttradingassistant.vo.BacktestTradeVO;
import java.util.List;

/**
 * 回测服务接口
 *
 * @author Rocs
 * @since 2026/05/29
 */
public interface BacktestService {

    /**
     * 运行策略回测
     *
     * @param authorization HTTP Authorization 请求头
     * @param request 运行回测请求参数
     * @return 回测记录
     */
    BacktestRecordVO runBacktest(String authorization, BacktestRunRequest request);

    /**
     * 查询当前用户回测记录列表
     *
     * @param authorization HTTP Authorization 请求头
     * @return 回测记录列表
     */
    List<BacktestRecordVO> listBacktests(String authorization);

    /**
     * 查询回测记录详情
     *
     * @param authorization HTTP Authorization 请求头
     * @param id 回测ID
     * @return 回测记录详情
     */
    BacktestRecordVO getBacktest(String authorization, Long id);

    /**
     * 查询回测成交记录
     *
     * @param authorization HTTP Authorization 请求头
     * @param id 回测ID
     * @return 回测成交记录列表
     */
    List<BacktestTradeVO> listTrades(String authorization, Long id);
}
