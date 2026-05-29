package com.rocs.quanttradingassistant.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 策略配置展示对象
 *
 * @author Rocs
 * @since 2026/05/29
 */
@Schema(description = "策略配置展示对象")
public class StrategyVO {

    @Schema(description = "策略ID", example = "1")
    private Long id;

    @Schema(description = "策略名称", example = "双均线策略")
    private String name;

    @Schema(description = "策略编码", example = "ma_cross_default")
    private String code;

    @Schema(description = "策略类型", example = "MA_CROSS")
    private String type;

    @Schema(description = "策略类型说明", example = "双均线策略")
    private String typeDescription;

    @Schema(description = "策略说明", example = "短期均线上穿长期均线时形成买入信号")
    private String description;

    @Schema(description = "是否启用", example = "true")
    private Boolean enabled;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;

    @Schema(description = "策略参数")
    private List<StrategyParamVO> params;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getTypeDescription() {
        return typeDescription;
    }

    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<StrategyParamVO> getParams() {
        return params;
    }

    public void setParams(List<StrategyParamVO> params) {
        this.params = params;
    }
}
