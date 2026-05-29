package com.rocs.quanttradingassistant.backtest;

import com.rocs.quanttradingassistant.common.TradeSide;
import java.time.LocalDate;

/**
 * 策略交易信号
 *
 * @author Rocs
 * @since 2026/05/29
 */
public class TradingSignal {

    private LocalDate tradeDate;

    private TradeSide side;

    private String reason;

    public TradingSignal() {
    }

    public TradingSignal(LocalDate tradeDate, TradeSide side, String reason) {
        this.tradeDate = tradeDate;
        this.side = side;
        this.reason = reason;
    }

    public LocalDate getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(LocalDate tradeDate) {
        this.tradeDate = tradeDate;
    }

    public TradeSide getSide() {
        return side;
    }

    public void setSide(TradeSide side) {
        this.side = side;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
