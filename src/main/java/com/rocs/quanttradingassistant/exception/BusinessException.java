package com.rocs.quanttradingassistant.exception;

import com.rocs.quanttradingassistant.common.ResultCode;

/**
 * 业务异常类
 *
 * @author Rocs
 * @since 2026/05/28
 */
public class BusinessException extends RuntimeException {

    private final ResultCode resultCode;

    public BusinessException(ResultCode resultCode, String message) {
        super(message);
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}
