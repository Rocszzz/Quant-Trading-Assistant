package com.rocs.quanttradingassistant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 更新策略参数请求对象
 *
 * @author Rocs
 * @since 2026/05/29
 */
@Schema(description = "更新策略参数请求对象")
public class StrategyParamUpdateRequest {

    @Schema(description = "策略参数列表")
    @Valid
    @NotNull(message = "策略参数列表不能为空")
    private List<StrategyParamRequest> params;

    public List<StrategyParamRequest> getParams() {
        return params;
    }

    public void setParams(List<StrategyParamRequest> params) {
        this.params = params;
    }
}
