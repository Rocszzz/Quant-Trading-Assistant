package com.rocs.quanttradingassistant.service;

import com.rocs.quanttradingassistant.dto.WatchlistAddRequest;
import com.rocs.quanttradingassistant.vo.WatchlistVO;
import java.util.List;

/**
 * 用户自选股服务接口
 *
 * @author Rocs
 * @since 2026/05/28
 */
public interface WatchlistService {

    /**
     * 查询当前用户自选股列表
     *
     * @param authorization HTTP Authorization 请求头
     * @return 自选股列表
     */
    List<WatchlistVO> listWatchlist(String authorization);

    /**
     * 添加当前用户自选股
     *
     * @param authorization HTTP Authorization 请求头
     * @param request 添加自选股请求参数
     * @return 自选股信息
     */
    WatchlistVO addWatchlist(String authorization, WatchlistAddRequest request);

    /**
     * 删除当前用户自选股
     *
     * @param authorization HTTP Authorization 请求头
     * @param id 自选股记录ID
     */
    void deleteWatchlist(String authorization, Long id);
}
