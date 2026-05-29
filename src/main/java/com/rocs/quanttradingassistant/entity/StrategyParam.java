package com.rocs.quanttradingassistant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 策略参数实体类
 *
 * @author Rocs
 * @since 2026/05/29
 */
@TableName("strategy_param")
public class StrategyParam {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long strategyId;

    private String paramKey;

    private String paramValue;

    private String paramType;

    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Long strategyId) {
        this.strategyId = strategyId;
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

    @Override
    public String toString() {
        return "StrategyParam{"
                + "id=" + id
                + ", strategyId=" + strategyId
                + ", paramKey='" + paramKey + '\''
                + ", paramType='" + paramType + '\''
                + '}';
    }
}
