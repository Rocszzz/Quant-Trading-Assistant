package com.rocs.quanttradingassistant.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.rocs.quanttradingassistant.common.ResultCode;
import com.rocs.quanttradingassistant.common.StrategyType;
import com.rocs.quanttradingassistant.dto.StrategyCreateRequest;
import com.rocs.quanttradingassistant.dto.StrategyParamRequest;
import com.rocs.quanttradingassistant.dto.StrategyParamUpdateRequest;
import com.rocs.quanttradingassistant.dto.StrategyUpdateRequest;
import com.rocs.quanttradingassistant.entity.Strategy;
import com.rocs.quanttradingassistant.entity.StrategyParam;
import com.rocs.quanttradingassistant.exception.BusinessException;
import com.rocs.quanttradingassistant.mapper.StrategyMapper;
import com.rocs.quanttradingassistant.mapper.StrategyParamMapper;
import com.rocs.quanttradingassistant.service.AuthService;
import com.rocs.quanttradingassistant.service.StrategyService;
import com.rocs.quanttradingassistant.vo.StrategyParamVO;
import com.rocs.quanttradingassistant.vo.StrategyVO;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 策略配置服务实现类
 *
 * @author Rocs
 * @since 2026/05/29
 */
@Service
public class StrategyServiceImpl implements StrategyService {

    private final AuthService authService;

    private final StrategyMapper strategyMapper;

    private final StrategyParamMapper strategyParamMapper;

    public StrategyServiceImpl(
            AuthService authService,
            StrategyMapper strategyMapper,
            StrategyParamMapper strategyParamMapper
    ) {
        this.authService = authService;
        this.strategyMapper = strategyMapper;
        this.strategyParamMapper = strategyParamMapper;
    }

    @Override
    public List<StrategyVO> listStrategies(String authorization) {
        Long userId = authService.getCurrentUserId(authorization);
        List<Strategy> strategies = strategyMapper.selectList(Wrappers.<Strategy>lambdaQuery()
                .eq(Strategy::getUserId, userId)
                .orderByDesc(Strategy::getCreatedAt));
        if (strategies.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> strategyIds = strategies.stream()
                .map(Strategy::getId)
                .toList();
        Map<Long, List<StrategyParam>> paramMap = strategyParamMapper.selectList(Wrappers.<StrategyParam>lambdaQuery()
                        .in(StrategyParam::getStrategyId, strategyIds)
                        .orderByAsc(StrategyParam::getId))
                .stream()
                .collect(Collectors.groupingBy(StrategyParam::getStrategyId));
        return strategies.stream()
                .map(strategy -> toVO(strategy, paramMap.getOrDefault(strategy.getId(), Collections.emptyList())))
                .toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StrategyVO createStrategy(String authorization, StrategyCreateRequest request) {
        Long userId = authService.getCurrentUserId(authorization);
        checkStrategyType(request.getType());
        checkParamKeys(request.getParams());
        checkCodeUnique(userId, null, request.getCode());

        LocalDateTime now = LocalDateTime.now();
        Strategy strategy = new Strategy();
        strategy.setUserId(userId);
        strategy.setName(request.getName());
        strategy.setCode(request.getCode());
        strategy.setType(request.getType());
        strategy.setDescription(request.getDescription());
        strategy.setEnabled(request.getEnabled());
        strategy.setCreatedAt(now);
        strategy.setUpdatedAt(now);
        strategyMapper.insert(strategy);

        saveParams(strategy.getId(), request.getParams());
        return getStrategy(authorization, strategy.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StrategyVO updateStrategy(String authorization, Long id, StrategyUpdateRequest request) {
        Long userId = authService.getCurrentUserId(authorization);
        Strategy strategy = getOwnedStrategy(userId, id);
        checkStrategyType(request.getType());
        checkCodeUnique(userId, id, request.getCode());

        strategy.setName(request.getName());
        strategy.setCode(request.getCode());
        strategy.setType(request.getType());
        strategy.setDescription(request.getDescription());
        strategy.setEnabled(request.getEnabled());
        strategy.setUpdatedAt(LocalDateTime.now());
        strategyMapper.updateById(strategy);
        return getStrategy(authorization, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteStrategy(String authorization, Long id) {
        Long userId = authService.getCurrentUserId(authorization);
        getOwnedStrategy(userId, id);
        strategyParamMapper.delete(Wrappers.<StrategyParam>lambdaQuery()
                .eq(StrategyParam::getStrategyId, id));
        strategyMapper.deleteById(id);
    }

    @Override
    public StrategyVO getStrategy(String authorization, Long id) {
        Long userId = authService.getCurrentUserId(authorization);
        Strategy strategy = getOwnedStrategy(userId, id);
        List<StrategyParam> params = listParams(id);
        return toVO(strategy, params);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StrategyVO updateParams(String authorization, Long id, StrategyParamUpdateRequest request) {
        Long userId = authService.getCurrentUserId(authorization);
        Strategy strategy = getOwnedStrategy(userId, id);
        checkParamKeys(request.getParams());

        // 参数采用整体覆盖，避免旧参数残留影响后续回测配置读取。
        strategyParamMapper.delete(Wrappers.<StrategyParam>lambdaQuery()
                .eq(StrategyParam::getStrategyId, id));
        saveParams(id, request.getParams());
        strategy.setUpdatedAt(LocalDateTime.now());
        strategyMapper.updateById(strategy);
        return getStrategy(authorization, id);
    }

    private Strategy getOwnedStrategy(Long userId, Long id) {
        Strategy strategy = strategyMapper.selectById(id);
        if (strategy == null || !userId.equals(strategy.getUserId())) {
            throw new BusinessException(ResultCode.NOT_FOUND, "策略不存在");
        }
        return strategy;
    }

    private List<StrategyParam> listParams(Long strategyId) {
        return strategyParamMapper.selectList(Wrappers.<StrategyParam>lambdaQuery()
                .eq(StrategyParam::getStrategyId, strategyId)
                .orderByAsc(StrategyParam::getId));
    }

    private void checkStrategyType(String type) {
        if (!StrategyType.isSupported(type)) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "策略类型只支持MA_CROSS、MACD、BREAKOUT");
        }
    }

    private void checkCodeUnique(Long userId, Long strategyId, String code) {
        Long existsCount = strategyMapper.selectCount(Wrappers.<Strategy>lambdaQuery()
                .eq(Strategy::getUserId, userId)
                .eq(Strategy::getCode, code)
                .ne(strategyId != null, Strategy::getId, strategyId));
        if (existsCount > 0) {
            throw new BusinessException(ResultCode.CONFLICT, "策略编码已存在");
        }
    }

    private void checkParamKeys(List<StrategyParamRequest> params) {
        if (params == null || params.isEmpty()) {
            return;
        }

        Set<String> paramKeys = new HashSet<>(params.size());
        for (StrategyParamRequest param : params) {
            if (!paramKeys.add(param.getParamKey())) {
                throw new BusinessException(ResultCode.BAD_REQUEST, "策略参数键不能重复");
            }
        }
    }

    private void saveParams(Long strategyId, List<StrategyParamRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            return;
        }

        for (StrategyParamRequest request : requests) {
            StrategyParam strategyParam = new StrategyParam();
            strategyParam.setStrategyId(strategyId);
            strategyParam.setParamKey(request.getParamKey());
            strategyParam.setParamValue(request.getParamValue());
            strategyParam.setParamType(request.getParamType());
            strategyParam.setRemark(request.getRemark());
            strategyParamMapper.insert(strategyParam);
        }
    }

    private StrategyVO toVO(Strategy strategy, List<StrategyParam> params) {
        StrategyVO strategyVO = new StrategyVO();
        strategyVO.setId(strategy.getId());
        strategyVO.setName(strategy.getName());
        strategyVO.setCode(strategy.getCode());
        strategyVO.setType(strategy.getType());
        strategyVO.setTypeDescription(StrategyType.valueOf(strategy.getType()).getDescription());
        strategyVO.setDescription(strategy.getDescription());
        strategyVO.setEnabled(strategy.getEnabled());
        strategyVO.setCreatedAt(strategy.getCreatedAt());
        strategyVO.setUpdatedAt(strategy.getUpdatedAt());
        strategyVO.setParams(params.stream()
                .map(this::toParamVO)
                .toList());
        return strategyVO;
    }

    private StrategyParamVO toParamVO(StrategyParam strategyParam) {
        StrategyParamVO strategyParamVO = new StrategyParamVO();
        strategyParamVO.setId(strategyParam.getId());
        strategyParamVO.setParamKey(strategyParam.getParamKey());
        strategyParamVO.setParamValue(strategyParam.getParamValue());
        strategyParamVO.setParamType(strategyParam.getParamType());
        strategyParamVO.setRemark(strategyParam.getRemark());
        return strategyParamVO;
    }
}
