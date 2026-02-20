package org.example.jobservice.config;

import org.example.common.result.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * 捕获所有未处理的异常，统一返回 JSON 格式，避免直接返回 500 页面
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常（IllegalStateException / IllegalArgumentException）
     */
    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.OK)
    public Result<?> handleBusinessException(Exception e) {
        e.printStackTrace();
        return Result.fail(400, e.getMessage());
    }

    /**
     * 请求体解析失败（JSON 格式错误、类型不匹配等）
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<?> handleMessageNotReadable(HttpMessageNotReadableException e) {
        e.printStackTrace();
        return Result.fail(400, "请求参数格式错误：" + e.getMostSpecificCause().getMessage());
    }

    /**
     * 兜底：所有未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<?> handleException(Exception e) {
        e.printStackTrace();
        return Result.fail(500, "服务器内部错误：" + e.getMessage());
    }
}










