package com.rocs.quanttradingassistant.common;

/**
 * 统一接口响应结构
 *
 * @param <T> 响应数据类型
 * @author Rocs
 * @since 2026/05/28
 */
public class Result<T> {

    private Integer code;

    private String message;

    private T data;

    public Result() {
    }

    private Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 构造成功响应
     *
     * @param data 响应数据
     * @param <T> 响应数据类型
     * @return 统一响应
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 构造无数据成功响应
     *
     * @return 统一响应
     */
    public static Result<Void> success() {
        return success(null);
    }

    /**
     * 构造失败响应
     *
     * @param resultCode 响应状态码
     * @param message 错误信息
     * @param <T> 响应数据类型
     * @return 统一响应
     */
    public static <T> Result<T> fail(ResultCode resultCode, String message) {
        return new Result<>(resultCode.getCode(), message, null);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
