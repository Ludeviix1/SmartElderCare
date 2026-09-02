package com.elder.exception;

import com.elder.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理：controller 及其下层(service、mapper)抛出的任何异常
 * 都会被这里拦截，统一包装成 Result 返回给前端
 * 前端 result.code !== 1 时会弹出 result.msg
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //业务异常：代码里主动抛出，msg 就是给用户看的提示
    @ExceptionHandler(ServiceException.class)
    public Result serviceException(ServiceException e) {
        log.warn("业务异常: {}", e.getMessage());
        return Result.error(e.getMessage());
    }

    //兜底：意料之外的异常，详细信息只记日志，不暴露给前端
    @ExceptionHandler(Exception.class)
    public Result exception(Exception e) {
        log.error("系统异常", e);
        return Result.error("系统繁忙，请稍后重试");
    }
}
