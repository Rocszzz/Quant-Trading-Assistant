package com.rocs.quanttradingassistant.service;

import com.rocs.quanttradingassistant.dto.StrategyCreateRequest;
import com.rocs.quanttradingassistant.dto.StrategyParamUpdateRequest;
import com.rocs.quanttradingassistant.dto.StrategyUpdateRequest;
import com.rocs.quanttradingassistant.vo.StrategyVO;
import java.util.List;

/**
 * 策略配置服务接口
 *
 * @author Rocs
 * @since 2026/05/29
 */
public interface StrategyService {

    /**
     * 查询当前用户策略列表
     *
     * @param authorization HTTP Authorization 请求头
     * @return 策略列表
     */
    List<StrategyVO> listStrategies(String authorization);

    /**
     * 查询当前用户已启用策略列表
     *
     * @param authorization HTTP Authorization 请求头
     * @return 已启用策略列表
     */
    List<StrategyVO> listEnabledStrategies(String authorization);

    /**
     * 创建策略配置
     *
     * @param authorization HTTP Authorization 请求头
     * @param request 创建策略请求参数
     * @return 策略配置
     */
    StrategyVO createStrategy(String authorization, StrategyCreateRequest request);

    /**
     * 更新策略配置
     *
     * @param authorization HTTP Authorization 请求头
     * @param id 策略ID
     * @param request 更新策略请求参数
     * @return 策略配置
     */
    StrategyVO updateStrategy(String authorization, Long id, StrategyUpdateRequest request);

    /**
     * 删除策略配置及参数
     *
     * @param authorization HTTP Authorization 请求头
     * @param id 策略ID
     */
    void deleteStrategy(String authorization, Long id);

    /**
     * 查询策略详情
     *
     * @param authorization HTTP Authorization 请求头
     * @param id 策略ID
     * @return 策略配置详情
     */
    StrategyVO getStrategy(String authorization, Long id);

    /**
     * 覆盖更新策略参数
     *
     * @param authorization HTTP Authorization 请求头
     * @param id 策略ID
     * @param request 策略参数请求参数
     * @return 策略配置详情
     */
    StrategyVO updateParams(String authorization, Long id, StrategyParamUpdateRequest request);
}
