package com.rocs.quanttradingassistant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * 创建策略请求参数
 *
 * @author Rocs
 * @since 2026/05/29
 */
@Schema(description = "创建策略请求参数")
public class StrategyCreateRequest {

    @Schema(description = "策略名称", example = "双均线策略")
    @NotBlank(message = "策略名称不能为空")
    @Size(max = 64, message = "策略名称长度不能超过64个字符")
    private String name;

    @Schema(description = "策略编码，同一用户下唯一", example = "ma_cross_default")
    @NotBlank(message = "策略编码不能为空")
    @Pattern(regexp = "^[A-Za-z0-9_-]{2,64}$", message = "策略编码只能包含字母、数字、下划线和中划线，长度为2到64个字符")
    private String code;

    @Schema(description = "策略类型：MA_CROSS、MACD、BREAKOUT", example = "MA_CROSS")
    @NotBlank(message = "策略类型不能为空")
    private String type;

    @Schema(description = "策略说明", example = "短期均线上穿长期均线时形成买入信号")
    @Size(max = 512, message = "策略说明长度不能超过512个字符")
    private String description;

    @Schema(description = "是否启用", example = "true")
    @NotNull(message = "启用状态不能为空")
    private Boolean enabled;

    @Schema(description = "策略参数")
    @Valid
    private List<StrategyParamRequest> params;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<StrategyParamRequest> getParams() {
        return params;
    }

    public void setParams(List<StrategyParamRequest> params) {
        this.params = params;
    }
}
