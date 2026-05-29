package com.rocs.quanttradingassistant.common;

/**
 * 策略类型枚举
 *
 * @author Rocs
 * @since 2026/05/29
 */
public enum StrategyType {

    /**
     * 双均线策略
     */
    MA_CROSS("双均线策略"),

    /**
     * MACD 策略
     */
    MACD("MACD 策略"),

    /**
     * 突破策略
     */
    BREAKOUT("突破策略");

    private final String description;

    StrategyType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 判断策略类型是否支持
     *
     * @param type 策略类型编码
     * @return 是否支持
     */
    public static boolean isSupported(String type) {
        for (StrategyType strategyType : values()) {
            if (strategyType.name().equals(type)) {
                return true;
            }
        }
        return false;
    }
}
