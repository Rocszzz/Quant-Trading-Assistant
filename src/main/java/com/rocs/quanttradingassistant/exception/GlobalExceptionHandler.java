package com.rocs.quanttradingassistant.exception;

import com.rocs.quanttradingassistant.common.Result;
import com.rocs.quanttradingassistant.common.ResultCode;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author Rocs
 * @since 2026/05/28
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理业务异常
     *
     * @param exception 业务异常
     * @return 统一响应
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException exception) {
        logger.warn("business exception: {}", exception.getMessage());
        return Result.fail(exception.getResultCode(), exception.getMessage());
    }

    /**
     * 处理请求体参数校验异常
     *
     * @param exception 参数校验异常
     * @return 统一响应
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return Result.fail(ResultCode.BAD_REQUEST, getFieldErrorMessage(exception));
    }

    /**
     * 处理绑定参数校验异常
     *
     * @param exception 绑定异常
     * @return 统一响应
     */
    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException exception) {
        return Result.fail(ResultCode.BAD_REQUEST, getFieldErrorMessage(exception));
    }

    /**
     * 处理单个参数校验异常
     *
     * @param exception 参数约束异常
     * @return 统一响应
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Void> handleConstraintViolationException(ConstraintViolationException exception) {
        return Result.fail(ResultCode.BAD_REQUEST, exception.getMessage());
    }

    /**
     * 处理系统兜底异常
     *
     * @param exception 未处理异常
     * @return 统一响应
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception exception) {
        logger.error("unexpected system exception", exception);
        return Result.fail(ResultCode.INTERNAL_ERROR, ResultCode.INTERNAL_ERROR.getMessage());
    }

    private String getFieldErrorMessage(BindException exception) {
        FieldError fieldError = exception.getBindingResult().getFieldError();
        if (fieldError == null) {
            return ResultCode.BAD_REQUEST.getMessage();
        }
        return fieldError.getDefaultMessage();
    }
}
