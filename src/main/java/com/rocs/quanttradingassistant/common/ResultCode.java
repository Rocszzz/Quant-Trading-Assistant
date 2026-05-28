package com.rocs.quanttradingassistant.common;

/**
 * 统一响应状态码枚举
 *
 * @author Rocs
 * @since 2026/05/28
 */
public enum ResultCode {

    /**
     * 请求成功
     */
    SUCCESS(200, "success"),

    /**
     * 请求参数错误
     */
    BAD_REQUEST(400, "请求参数错误"),

    /**
     * 用户未认证或登录已失效
     */
    UNAUTHORIZED(401, "登录状态已失效"),

    /**
     * 业务处理失败
     */
    BUSINESS_ERROR(5001, "业务处理失败"),

    /**
     * 系统内部异常
     */
    INTERNAL_ERROR(500, "系统异常");

    private final Integer code;

    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
