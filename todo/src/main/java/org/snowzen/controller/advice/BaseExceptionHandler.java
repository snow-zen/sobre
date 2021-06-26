package org.snowzen.controller.advice;

import lombok.extern.slf4j.Slf4j;
import org.snowzen.model.ApiResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * 基础异常处理器
 *
 * @author snow-zen
 */
@Slf4j
@RestControllerAdvice
public class BaseExceptionHandler {

    @ExceptionHandler({NullPointerException.class, IllegalArgumentException.class})
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ApiResult handlerException(Exception e) {
        log.error("系统错误", e);
        return ApiResult.fail(INTERNAL_SERVER_ERROR.value(), "系统错误");
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiResult handlerIllegalState(IllegalStateException e) {
        log.error("状态异常", e);
        return ApiResult.fail(BAD_REQUEST.value(), e.getMessage());
    }
}
