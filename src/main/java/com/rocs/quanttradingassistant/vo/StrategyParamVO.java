package com.rocs.quanttradingassistant.vo;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 策略参数展示对象
 *
 * @author Rocs
 * @since 2026/05/29
 */
@Schema(description = "策略参数展示对象")
public class StrategyParamVO {

    @Schema(description = "参数ID", example = "1")
    private Long id;

    @Schema(description = "参数键", example = "shortPeriod")
    private String paramKey;

    @Schema(description = "参数值", example = "5")
    private String paramValue;

    @Schema(description = "参数类型", example = "NUMBER")
    private String paramType;

    @Schema(description = "参数说明", example = "短期均线周期")
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
