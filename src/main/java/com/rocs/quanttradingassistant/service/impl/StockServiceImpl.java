package com.rocs.quanttradingassistant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.rocs.quanttradingassistant.common.StockStatus;
import com.rocs.quanttradingassistant.dto.StockSearchRequest;
import com.rocs.quanttradingassistant.entity.StockInfo;
import com.rocs.quanttradingassistant.mapper.StockInfoMapper;
import com.rocs.quanttradingassistant.service.StockService;
import com.rocs.quanttradingassistant.vo.StockInfoVO;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 股票基础信息服务实现类
 *
 * @author Rocs
 * @since 2026/05/28
 */
@Service
public class StockServiceImpl implements StockService {

    private final StockInfoMapper stockInfoMapper;

    public StockServiceImpl(StockInfoMapper stockInfoMapper) {
        this.stockInfoMapper = stockInfoMapper;
    }

    @Override
    public List<StockInfoVO> listStocks() {
        LambdaQueryWrapper<StockInfo> queryWrapper = Wrappers.<StockInfo>lambdaQuery()
                .eq(StockInfo::getStatus, StockStatus.ACTIVE.name())
                .orderByAsc(StockInfo::getSymbol);
        return stockInfoMapper.selectList(queryWrapper).stream()
                .map(this::toVO)
                .toList();
    }

    @Override
    public List<StockInfoVO> searchStocks(StockSearchRequest request) {
        String keyword = request.getKeyword();
        if (!StringUtils.hasText(keyword)) {
            return listStocks();
        }

        LambdaQueryWrapper<StockInfo> queryWrapper = Wrappers.<StockInfo>lambdaQuery()
                .eq(StockInfo::getStatus, StockStatus.ACTIVE.name())
                .and(wrapper -> wrapper
                        .like(StockInfo::getSymbol, keyword)
                        .or()
                        .like(StockInfo::getName, keyword))
                .orderByAsc(StockInfo::getSymbol);
        return stockInfoMapper.selectList(queryWrapper).stream()
                .map(this::toVO)
                .toList();
    }

    private StockInfoVO toVO(StockInfo stockInfo) {
        StockInfoVO stockInfoVO = new StockInfoVO();
        stockInfoVO.setId(stockInfo.getId());
        stockInfoVO.setSymbol(stockInfo.getSymbol());
        stockInfoVO.setName(stockInfo.getName());
        stockInfoVO.setExchange(stockInfo.getExchange());
        stockInfoVO.setIndustry(stockInfo.getIndustry());
        stockInfoVO.setStatus(stockInfo.getStatus());
        stockInfoVO.setCreatedAt(stockInfo.getCreatedAt());
        stockInfoVO.setUpdatedAt(stockInfo.getUpdatedAt());
        return stockInfoVO;
    }
}
