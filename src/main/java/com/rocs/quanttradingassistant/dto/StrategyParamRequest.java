package com.rocs.quanttradingassistant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 策略参数请求对象
 *
 * @author Rocs
 * @since 2026/05/29
 */
@Schema(description = "策略参数请求对象")
public class StrategyParamRequest {

    @Schema(description = "参数键", example = "shortPeriod")
    @NotBlank(message = "参数键不能为空")
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9_]{1,63}$", message = "参数键必须以字母开头，只能包含字母、数字和下划线，长度为2到64个字符")
    private String paramKey;

    @Schema(description = "参数值", example = "5")
    @NotBlank(message = "参数值不能为空")
    @Size(max = 256, message = "参数值长度不能超过256个字符")
    private String paramValue;

    @Schema(description = "参数类型：NUMBER、STRING、BOOLEAN", example = "NUMBER")
    @NotBlank(message = "参数类型不能为空")
    @Pattern(regexp = "^(NUMBER|STRING|BOOLEAN)$", message = "参数类型只支持NUMBER、STRING、BOOLEAN")
    private String paramType;

    @Schema(description = "参数说明", example = "短期均线周期")
    @Size(max = 128, message = "参数说明长度不能超过128个字符")
    private String remark;

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
